"""
Engaje-se · Painel de Segmentação
----------------------------------
Dashboard para análise dos dados coletados pelo formulário Engaje-se.
Filtra, visualiza e exporta cadastros de voluntários por região,
perfil demográfico, temas de interesse e formas de participação.

Para rodar localmente:
    pip install streamlit pandas plotly
    streamlit run app.py
"""

import io
import os
from datetime import datetime

import pandas as pd
import plotly.express as px
import streamlit as st


# ─────────────────────────────────────────────
# CONFIGURAÇÃO DA PÁGINA
# ─────────────────────────────────────────────

st.set_page_config(
    page_title="Engaje-se · Segmentação",
    page_icon="🗳️",
    layout="wide",
    initial_sidebar_state="expanded",
)



SENHA_CORRETA = "engajese2026"  # troque para a senha que quiser

def tela_de_login():
    st.title("🗳️ Engaje-se · Segmentação")
    st.markdown("---")

    senha_digitada = st.text_input(
        "Digite a senha para acessar o painel",
        type="password",
        placeholder="Senha...",
    )

    if not senha_digitada:
        st.info("Este painel é restrito. Digite a senha para continuar.")
        st.stop()

    if senha_digitada != SENHA_CORRETA:
        st.error("Senha incorreta. Tente novamente.")
        st.stop()

tela_de_login()




FORMAS_DE_AJUDA = {
    "group":                   "Grupo de WPP",
    "mail":                    "Receber material",
    "manifestation":           "Manifestações",
    "meetings":                "Reuniões",
    "wannaBePSOL":             "Quero ser do PSOL",
    "participateinleafleting": "Panfletar",
    "isPSOL":                  "Já sou do PSOL",
}

TEMAS_DE_INTERESSE = {
    "education":         "Educação",
    "health":            "Saúde",
    "enviroment":        "Meio Ambiente",
    "culture":           "Cultura",
    "publicSecurity":    "Segurança Pública",
    "antiracism":        "Antirracismo",
    "agrarianReform":    "Reforma Agrária",
    "socialassistant":   "Assistência Social",
    "communication":     "Comunicação",
    "pwdRights":         "Direitos PcD",
    "feminism":          "Feminismo",
    "african_matrices":  "Matrizes Africanas",
    "urban_mobility":    "Mobilidade Urbana",
    "youth":             "Juventude",
    "lgbt":              "LGBT+",
    "informal_worker":   "Trabalhador Informal",
    "maternity":         "Maternidade",
    "antiprohibitionism":"Antiproibicionismo",
}

FAIXAS_ETARIAS = ["< 18", "18-24", "25-34", "35-44", "45-54", "55-64", "65+", "Não informado"]

CORES = ["#e63946", "#457b9d", "#2a9d8f", "#e9c46a", "#f4a261", "#264653", "#a8dadc"]

ALTURA_GRAFICO = 380  # altura padrão dos gráficos em pixels



st.markdown("""
<style>

  [data-testid="stSidebar"] { background: #0f172a !important; }
  [data-testid="stSidebar"] p,
  [data-testid="stSidebar"] label,
  [data-testid="stSidebar"] span,
  [data-testid="stSidebar"] div { color: #cbd5e1 !important; }
  [data-testid="stSidebar"] hr { border-color: #1e293b; }


  .card-kpi {
    background: #1e293b;
    border-left: 4px solid #e63946;
    border-radius: 8px;
    padding: 16px 20px;
    margin-bottom: 10px;
  }
  .card-kpi-label { font-size:.7rem; color:#94a3b8; letter-spacing:1.5px; text-transform:uppercase; margin:0 0 4px; }
  .card-kpi-valor { font-size:2.1rem; font-weight:800; color:#f1f5f9; margin:0; line-height:1.1; }
  .card-kpi-sub   { font-size:.7rem; color:#64748b; margin:4px 0 0; }


  .secao-titulo {
    font-size:.75rem; font-weight:700; color:#e63946;
    text-transform:uppercase; letter-spacing:2px;
    border-bottom:2px solid #e63946;
    padding-bottom:4px; margin:22px 0 10px;
  }


  button[data-baseweb="tab"][aria-selected="true"] { color:#e63946 !important; }
</style>
""", unsafe_allow_html=True)



def card_kpi(coluna, label, valor, sublabel=""):
    sublabel_html = f'<p class="card-kpi-sub">{sublabel}</p>' if sublabel else ""
    coluna.markdown(
        f'<div class="card-kpi">'
        f'<p class="card-kpi-label">{label}</p>'
        f'<p class="card-kpi-valor">{valor}</p>'
        f'{sublabel_html}'
        f'</div>',
        unsafe_allow_html=True,
    )


def cabecalho_secao(texto):
    st.markdown(f'<div class="secao-titulo">{texto}</div>', unsafe_allow_html=True)


def formatar_numero(n: int) -> str:
    return f"{n:,}".replace(",", ".")


def exportar_csv(df: pd.DataFrame) -> bytes:
    return df.to_csv(index=False).encode("utf-8-sig")


def grafico_barras_horizontal(df, eixo_x, eixo_y, cor="#e63946", altura=ALTURA_GRAFICO, **kwargs):
    fig = px.bar(
        df, x=eixo_x, y=eixo_y,
        orientation="h",
        color_discrete_sequence=[cor],
        height=altura,
        **kwargs,
    )
    fig.update_layout(
        yaxis={"categoryorder": "total ascending"},
        margin=dict(l=0, r=10, t=5, b=5),
        plot_bgcolor="rgba(0,0,0,0)",
    )
    return fig


CONFIG_GRAFICO = {"displayModeBar": False}










@st.cache_data(show_spinner="Processando os dados…")
def carregar_dados(conteudo_csv: bytes) -> pd.DataFrame:

    df = pd.read_csv(io.BytesIO(conteudo_csv))

    df["datetime"] = pd.to_datetime(df["datetime"], utc=True, errors="coerce")
    df["Data de Nascimento"] = pd.to_datetime(df["Data de Nascimento"], errors="coerce")

    hoje = datetime.now()

    def calcular_idade(data_nascimento):
        if pd.isna(data_nascimento):
            return None
        idade = (hoje - data_nascimento.replace(tzinfo=None)).days / 365.25
        return int(idade) if 0 <= idade <= 120 else None

    df["Idade"] = df["Data de Nascimento"].apply(calcular_idade)

    def classificar_faixa_etaria(idade):
        if pd.isna(idade):
            return "Não informado"
        idade = int(idade)
        if idade < 18: return "< 18"
        if idade < 25: return "18-24"
        if idade < 35: return "25-34"
        if idade < 45: return "35-44"
        if idade < 55: return "45-54"
        if idade < 65: return "55-64"
        return "65+"

    df["Faixa Etária"] = df["Idade"].apply(classificar_faixa_etaria)

    for coluna in ["Cidade", "Sou...", "genero", "Estado"]:
        df[coluna] = df[coluna].fillna("Não informado")


    for chave in FORMAS_DE_AJUDA:
        df[f"_ajuda_{chave}"] = df["Como quero ajudar"].fillna("").str.contains(chave, regex=False)

    for chave in TEMAS_DE_INTERESSE:
        df[f"_tema_{chave}"] = df["Temas de interesse"].fillna("").str.contains(chave, regex=False)


    df["Mês"] = df["datetime"].dt.to_period("M").astype(str)

    return df


# ─────────────────────────────────────────────


st.title("🗳️ Engaje-se · Painel de Segmentação")

arquivo = st.file_uploader(
    "Faça o upload do CSV exportado do formulário",
    type="csv",
    help="O arquivo não é armazenado em nenhum servidor. Ele fica apenas na sua sessão local.",
)

if not arquivo:
    st.info("⬆️ Faça o upload do arquivo `segmentacao-engaje-se.csv` para começar a análise.")
    st.caption("Seus dados não são enviados para nenhum servidor externo.")
    st.stop()

df = carregar_dados(arquivo.read())



with st.sidebar:
    st.markdown("## 🔍 Filtros")

    todos_estados = sorted(e for e in df["Estado"].unique() if e != "Não informado")
    estados_selecionados = st.multiselect("Estado", todos_estados, placeholder="Todos os estados")

    pool_cidades = df[df["Estado"].isin(estados_selecionados)]["Cidade"] if estados_selecionados else df["Cidade"]
    todas_cidades = sorted(c for c in pool_cidades.unique() if c != "Não informado")
    cidades_selecionadas = st.multiselect("Cidade", todas_cidades, placeholder="Todas as cidades")

    st.divider()

    todas_ocupacoes = sorted(o for o in df["Sou..."].unique() if o != "Não informado")
    ocupacoes_selecionadas = st.multiselect("Ocupação", todas_ocupacoes, placeholder="Todas")

    todos_generos = sorted(g for g in df["genero"].unique() if g != "Não informado")
    generos_selecionados = st.multiselect("Gênero", todos_generos, placeholder="Todos")

    idades_disponiveis = df["Idade"].dropna()
    faixa_idade = None
    incluir_sem_idade = True

    if len(idades_disponiveis) > 0:
        idade_minima = int(idades_disponiveis.min())
        idade_maxima = min(int(idades_disponiveis.max()), 100)
        faixa_idade = st.slider("Faixa etária", idade_minima, idade_maxima, (idade_minima, idade_maxima))
        incluir_sem_idade = st.checkbox("Incluir sem data de nascimento", value=True)

    st.divider()
    st.markdown("**Como quer ajudar** *(pode marcar vários)*")

    formas_selecionadas = [
        chave for chave, rotulo in FORMAS_DE_AJUDA.items()
        if st.checkbox(rotulo, key=f"ajuda_{chave}")
    ]

    st.divider()

    rotulos_temas = list(TEMAS_DE_INTERESSE.values())
    chaves_temas  = list(TEMAS_DE_INTERESSE.keys())
    temas_selecionados_rotulos = st.multiselect("Temas de interesse", rotulos_temas, placeholder="Todos os temas")
    temas_selecionados = [chaves_temas[rotulos_temas.index(r)] for r in temas_selecionados_rotulos]



filtrado = df.copy()

if estados_selecionados:
    filtrado = filtrado[filtrado["Estado"].isin(estados_selecionados)]

if cidades_selecionadas:
    filtrado = filtrado[filtrado["Cidade"].isin(cidades_selecionadas)]

if ocupacoes_selecionadas:
    filtrado = filtrado[filtrado["Sou..."].isin(ocupacoes_selecionadas)]

if generos_selecionados:
    filtrado = filtrado[filtrado["genero"].isin(generos_selecionados)]

if faixa_idade is not None:
    mascara_idade = filtrado["Idade"].between(faixa_idade[0], faixa_idade[1])
    if incluir_sem_idade:
        mascara_idade = mascara_idade | filtrado["Idade"].isna()
    filtrado = filtrado[mascara_idade]

if formas_selecionadas:
    mascara_ajuda = pd.Series(False, index=filtrado.index)
    for chave in formas_selecionadas:
        mascara_ajuda = mascara_ajuda | filtrado[f"_ajuda_{chave}"]
    filtrado = filtrado[mascara_ajuda]

for chave in temas_selecionados:
    filtrado = filtrado[filtrado[f"_tema_{chave}"]]


# ─────────────────────────────────────────────

st.caption(f"Mostrando **{formatar_numero(len(filtrado))}** de {formatar_numero(len(df))} cadastros")

col1, col2, col3, col4 = st.columns(4)

total_filtrado  = len(filtrado)
total_panfl     = int(filtrado["_ajuda_participateinleafleting"].sum())
total_cidades   = filtrado[filtrado["Cidade"] != "Não informado"]["Cidade"].nunique()
total_estados   = filtrado[filtrado["Estado"] != "Não informado"]["Estado"].nunique()

card_kpi(col1, "Cadastros filtrados", formatar_numero(total_filtrado), f"de {formatar_numero(len(df))} total")
card_kpi(col2, "Panfletadores",       formatar_numero(total_panfl),    f"{total_panfl/total_filtrado*100:.1f}% do filtrado" if total_filtrado else "—")
card_kpi(col3, "Cidades",            formatar_numero(total_cidades))
card_kpi(col4, "Estados",            str(total_estados))

st.markdown("---")


# ─────────────────────────────────────────────
# ABAS
# ─────────────────────────────────────────────

aba_visao_geral, aba_panfletadores, aba_dados = st.tabs([
    "📊 Visão Geral",
    "📰 Panfletadores",
    "📋 Dados & Export",
])


# ═════════════════════════════════════════════
# ABA 1 · VISÃO GERAL
# ═════════════════════════════════════════════

with aba_visao_geral:

    # Gráfico de área com cadastros ao longo do tempo
    cabecalho_secao("Cadastros ao longo do tempo")
    linha_do_tempo = (
        filtrado.groupby("Mês", observed=True)
        .size()
        .reset_index(name="Cadastros")
        .sort_values("Mês")
    )
    if not linha_do_tempo.empty:
        fig = px.area(linha_do_tempo, x="Mês", y="Cadastros",
                      color_discrete_sequence=["#e63946"], height=220)
        fig.update_layout(margin=dict(l=0, r=10, t=5, b=5), plot_bgcolor="rgba(0,0,0,0)")
        st.plotly_chart(fig, use_container_width=True, config=CONFIG_GRAFICO)

    # Distribuição por Estado e por Cidade
    col_estado, col_cidade = st.columns(2)

    with col_estado:
        cabecalho_secao("Por Estado")
        df_estados = (
            filtrado[filtrado["Estado"] != "Não informado"]["Estado"]
            .value_counts().head(20).reset_index()
        )
        df_estados.columns = ["Estado", "n"]
        st.plotly_chart(
            grafico_barras_horizontal(df_estados, "n", "Estado", "#e63946"),
            use_container_width=True, config=CONFIG_GRAFICO,
        )

    with col_cidade:
        cabecalho_secao("Top 20 Cidades")
        df_cidades = (
            filtrado[filtrado["Cidade"] != "Não informado"]["Cidade"]
            .value_counts().head(20).reset_index()
        )
        df_cidades.columns = ["Cidade", "n"]
        st.plotly_chart(
            grafico_barras_horizontal(df_cidades, "n", "Cidade", "#457b9d"),
            use_container_width=True, config=CONFIG_GRAFICO,
        )

    # Distribuição por Gênero e por Ocupação
    col_genero, col_ocupacao = st.columns(2)

    with col_genero:
        cabecalho_secao("Por Gênero")
        df_generos = (
            filtrado[filtrado["genero"] != "Não informado"]["genero"]
            .value_counts().reset_index()
        )
        df_generos.columns = ["Gênero", "n"]
        fig = px.pie(df_generos, names="Gênero", values="n",
                     color_discrete_sequence=CORES, height=ALTURA_GRAFICO)
        fig.update_traces(textposition="inside", textinfo="percent+label")
        fig.update_layout(showlegend=False, margin=dict(l=0, r=0, t=5, b=5))
        st.plotly_chart(fig, use_container_width=True, config=CONFIG_GRAFICO)

    with col_ocupacao:
        cabecalho_secao("Por Ocupação")
        df_ocupacoes = (
            filtrado[filtrado["Sou..."] != "Não informado"]["Sou..."]
            .value_counts().reset_index()
        )
        df_ocupacoes.columns = ["Ocupação", "n"]
        st.plotly_chart(
            grafico_barras_horizontal(df_ocupacoes, "n", "Ocupação", "#2a9d8f"),
            use_container_width=True, config=CONFIG_GRAFICO,
        )

    # Faixa Etária e Temas de Interesse
    col_idade, col_temas = st.columns(2)

    with col_idade:
        cabecalho_secao("Por Faixa Etária")
        faixas_presentes = [f for f in FAIXAS_ETARIAS if f in filtrado["Faixa Etária"].values]
        df_idades = (
            filtrado["Faixa Etária"]
            .value_counts()
            .reindex(faixas_presentes, fill_value=0)
            .reset_index()
        )
        df_idades.columns = ["Faixa", "n"]
        fig = px.bar(df_idades, x="Faixa", y="n",
                     labels={"n": "Pessoas", "Faixa": ""},
                     color_discrete_sequence=["#f4a261"], height=ALTURA_GRAFICO)
        fig.update_layout(margin=dict(l=0, r=10, t=5, b=5), plot_bgcolor="rgba(0,0,0,0)")
        st.plotly_chart(fig, use_container_width=True, config=CONFIG_GRAFICO)

    with col_temas:
        cabecalho_secao("Temas de Interesse")
        df_temas = pd.DataFrame({
            "Tema": list(TEMAS_DE_INTERESSE.values()),
            "n":    [int(filtrado[f"_tema_{chave}"].sum()) for chave in TEMAS_DE_INTERESSE],
        }).sort_values("n")
        st.plotly_chart(
            grafico_barras_horizontal(df_temas, "n", "Tema", "#e9c46a"),
            use_container_width=True, config=CONFIG_GRAFICO,
        )

    # Como as pessoas querem participar
    cabecalho_secao("Como Querem Ajudar")
    df_ajuda = pd.DataFrame({
        "Forma": list(FORMAS_DE_AJUDA.values()),
        "n":     [int(filtrado[f"_ajuda_{chave}"].sum()) for chave in FORMAS_DE_AJUDA],
    }).sort_values("n", ascending=False)
    fig = px.bar(df_ajuda, x="Forma", y="n",
                 labels={"n": "Pessoas", "Forma": ""},
                 color_discrete_sequence=["#e63946"], height=320)
    fig.update_layout(margin=dict(l=0, r=10, t=5, b=5), plot_bgcolor="rgba(0,0,0,0)")
    st.plotly_chart(fig, use_container_width=True, config=CONFIG_GRAFICO)


# ═════════════════════════════════════════════
# ABA 2 · PANFLETADORES
# ═════════════════════════════════════════════

with aba_panfletadores:
    panfletadores = filtrado[filtrado["_ajuda_participateinleafleting"]].copy()
    total_panfletadores = len(panfletadores)

    if total_panfletadores == 0:
        st.warning("Nenhum panfletador encontrado com os filtros atuais.")
    else:
        p1, p2, p3 = st.columns(3)
        card_kpi(p1, "Total de panfletadores",      formatar_numero(total_panfletadores))
        card_kpi(p2, "Cidades com panfletadores",   str(panfletadores[panfletadores["Cidade"] != "Não informado"]["Cidade"].nunique()))
        card_kpi(p3, "Estados com panfletadores",   str(panfletadores[panfletadores["Estado"] != "Não informado"]["Estado"].nunique()))

        st.markdown("")

        col_cid_panfl, col_est_panfl = st.columns(2)

        with col_cid_panfl:
            cabecalho_secao("Top 20 Cidades · Panfletadores")
            df_cid_p = (
                panfletadores[panfletadores["Cidade"] != "Não informado"]["Cidade"]
                .value_counts().head(20).reset_index()
            )
            df_cid_p.columns = ["Cidade", "n"]
            st.plotly_chart(
                grafico_barras_horizontal(df_cid_p, "n", "Cidade", "#e63946", altura=480),
                use_container_width=True, config=CONFIG_GRAFICO,
            )

        with col_est_panfl:
            cabecalho_secao("Por Estado · Panfletadores")
            df_est_p = (
                panfletadores[panfletadores["Estado"] != "Não informado"]["Estado"]
                .value_counts().reset_index()
            )
            df_est_p.columns = ["Estado", "n"]
            st.plotly_chart(
                grafico_barras_horizontal(df_est_p, "n", "Estado", "#457b9d", altura=480),
                use_container_width=True, config=CONFIG_GRAFICO,
            )

        # Tabela detalhada por cidade
        cabecalho_secao("Tabela · Panfletadores por Cidade")
        tabela_cidades = (
            panfletadores[panfletadores["Cidade"] != "Não informado"]
            .groupby(["Estado", "Cidade"])
            .agg(
                Panfletadores=("Nome",    "count"),
                Feminino     =("genero",  lambda x: (x == "Feminino").sum()),
                Masculino    =("genero",  lambda x: (x == "Masculino").sum()),
                Aposentado   =("Sou...",  lambda x: (x == "Aposentado").sum()),
                Servidor     =("Sou...",  lambda x: (x == "Servidor público").sum()),
            )
            .reset_index()
            .sort_values("Panfletadores", ascending=False)
        )
        st.dataframe(tabela_cidades, use_container_width=True, hide_index=True)

        # Exportação dos panfletadores
        colunas_export = ["Nome", "E-mail", "Telefone", "Estado", "Cidade",
                          "genero", "Sou...", "Idade", "Faixa Etária", "Como quero ajudar"]
        colunas_export = [c for c in colunas_export if c in panfletadores.columns]

        st.download_button(
            "📥 Baixar CSV · Panfletadores",
            data=exportar_csv(panfletadores[colunas_export]),
            file_name="panfletadores.csv",
            mime="text/csv",
            use_container_width=True,
        )






with aba_dados:
    cabecalho_secao("Visualizar Dados")

    # Remove colunas internas (as que começam com _ e a coluna Mês)
    colunas_visiveis = [c for c in filtrado.columns if not c.startswith("_") and c != "Mês"]

    colunas_padrao = [c for c in [
        "Nome", "E-mail", "Telefone", "Estado", "Cidade",
        "genero", "Sou...", "Idade", "Faixa Etária",
        "Como quero ajudar", "Temas de interesse",
    ] if c in colunas_visiveis]

    colunas_escolhidas = st.multiselect("Colunas a exibir", colunas_visiveis, default=colunas_padrao)

    df_exibicao = filtrado[colunas_escolhidas] if colunas_escolhidas else filtrado[colunas_visiveis]
    st.dataframe(df_exibicao, use_container_width=True, hide_index=True, height=400)

    st.markdown("---")
    cabecalho_secao("Exportar CSV")

    df_para_export = filtrado[[c for c in filtrado.columns if not c.startswith("_") and c != "Mês"]]

    col_exp1, col_exp2, col_exp3 = st.columns(3)

    col_exp1.download_button(
        "📥 Filtrado completo",
        data=exportar_csv(df_para_export),
        file_name="filtrado.csv",
        mime="text/csv",
        use_container_width=True,
        help="Todos os campos, com os filtros aplicados",
    )

    # Exportação agrupada por cidade
    agrupado_por_cidade = (
        filtrado[filtrado["Cidade"] != "Não informado"]
        .groupby(["Estado", "Cidade"])
        .agg(
            Total        =("Nome",                        "count"),
            Panfletadores=("_ajuda_participateinleafleting", "sum"),
            Feminino     =("genero", lambda x: (x == "Feminino").sum()),
            Masculino    =("genero", lambda x: (x == "Masculino").sum()),
        )
        .reset_index()
        .sort_values("Panfletadores", ascending=False)
    )
    col_exp2.download_button(
        "📥 Agrupado por cidade",
        data=exportar_csv(agrupado_por_cidade),
        file_name="por_cidade.csv",
        mime="text/csv",
        use_container_width=True,
        help="Total, panfletadores, gênero — uma linha por cidade",
    )

    # Só panfletadores, sem colunas internas
    so_panfletadores = filtrado[filtrado["_ajuda_participateinleafleting"]]
    so_panfletadores = so_panfletadores[[c for c in so_panfletadores.columns if not c.startswith("_") and c != "Mês"]]

    col_exp3.download_button(
        "📥 Só panfletadores",
        data=exportar_csv(so_panfletadores),
        file_name="panfletadores.csv",
        mime="text/csv",
        use_container_width=True,
    )

    st.markdown("---")

    col_por_est, col_por_cid = st.columns(2)

    with col_por_est:
        cabecalho_secao("Exportar por Estado")
        estados_disponiveis = sorted(filtrado[filtrado["Estado"] != "Não informado"]["Estado"].unique())
        if estados_disponiveis:
            estado_escolhido = st.selectbox("Estado", estados_disponiveis)
            df_estado = filtrado[filtrado["Estado"] == estado_escolhido]
            df_estado = df_estado[[c for c in df_estado.columns if not c.startswith("_") and c != "Mês"]]
            st.caption(f"{formatar_numero(len(df_estado))} pessoas · {estado_escolhido}")
            st.download_button(
                f"📥 CSV · {estado_escolhido}",
                data=exportar_csv(df_estado),
                file_name=f"estado_{estado_escolhido}.csv",
                mime="text/csv",
                use_container_width=True,
            )

    with col_por_cid:
        cabecalho_secao("Exportar por Cidade")
        cidades_disponiveis = sorted(filtrado[filtrado["Cidade"] != "Não informado"]["Cidade"].unique())
        if cidades_disponiveis:
            cidade_escolhida = st.selectbox("Cidade", cidades_disponiveis)
            df_cidade = filtrado[filtrado["Cidade"] == cidade_escolhida]
            df_cidade = df_cidade[[c for c in df_cidade.columns if not c.startswith("_") and c != "Mês"]]
            st.caption(f"{formatar_numero(len(df_cidade))} pessoas · {cidade_escolhida}")
            st.download_button(
                f"📥 CSV · {cidade_escolhida}",
                data=exportar_csv(df_cidade),
                file_name=f"cidade_{cidade_escolhida.replace(' ', '_')}.csv",
                mime="text/csv",
                use_container_width=True,
            )