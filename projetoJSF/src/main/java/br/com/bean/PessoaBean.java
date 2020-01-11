package br.com.bean;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.xml.bind.DatatypeConverter;

import com.google.gson.Gson;

import br.com.dao.DaoGeneric;
import br.com.entidades.Cidades;
import br.com.entidades.Estados;
import br.com.entidades.Pessoa;
import br.com.jpautil.JPAUtil;
import br.com.repository.IDaoPessoa;
import br.com.repository.IDaoPessoaImpl;

@ViewScoped
@ManagedBean(name = "pessoaBean")
public class PessoaBean {

	private Pessoa pessoa = new Pessoa();
	private DaoGeneric<Pessoa> daoGeneric = new DaoGeneric<Pessoa>();
	private List<Pessoa> pessoas = new ArrayList<Pessoa>();
	private IDaoPessoa iDaoPessoa = new IDaoPessoaImpl();
	private List<SelectItem> estados;
	private List<SelectItem> cidades;
	private Part arquivofoto;

	public String salvar() throws IOException { // SALVA
		// PROCESSAR IMAGEN
		
		byte[] imagem = getByte(arquivofoto.getInputStream());
		pessoa.setFotoIconBase64Original(imagem);
		BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imagem));
		int type = bufferedImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : bufferedImage.getType();
		int largura = 200;
		int altura = 200;
		
		BufferedImage resizedImage = new BufferedImage(altura, altura, type);
		Graphics g = resizedImage.createGraphics();
		g.drawImage(bufferedImage, 0, 0, largura, altura, null);
		g.dispose();
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		String extensao = arquivofoto.getContentType().split("\\/")[1];
		ImageIO.write(resizedImage, extensao, baos);
		
		String miniImagem = "data:" + arquivofoto.getContentType() + ";base64," + DatatypeConverter.printBase64Binary(baos.toByteArray());
		pessoa.setFotoIconBase64(miniImagem);
		pessoa.setExtensao(extensao);
		
		pessoa = daoGeneric.merge(pessoa);
		pessoa = new Pessoa();
		carregarPessoas();
		mostrarMsg("Castrado com Sucesso");
		return "";
	}

	private void mostrarMsg(String msg) { // METEDO PARA MOSTRAR MESSAGEM QUANDO FAZ UM CADASTRO
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage(msg);
		context.addMessage(null, message);
	}

	public String novo() { // NOVO
		pessoa = new Pessoa();
		return "";
	}

	public String limpar() { // limpar
		pessoa = new Pessoa();
		return "";
	}

	public String remove() { // REMOVE POR ID
		daoGeneric.deletePorId(pessoa);
		pessoa = new Pessoa();
		carregarPessoas();
		mostrarMsg("Removido com Sucesso");
		return "";
	}

	@PostConstruct // SERVE PARA CARREGAR AUTOMATICAMENTE
	public void carregarPessoas() { // CARREGAR LISTA DE PESSOAS
		pessoas = daoGeneric.getListEntity(Pessoa.class);
	}

	public String logar() { // METEDO PARA LOGAR AUTENTICAR USUARIO

		Pessoa pessoaUser = iDaoPessoa.consultarUusario(pessoa.getLogin(), pessoa.getSenha());
		if (pessoaUser != null) { // ACHOU USUARIO
			// ADCIONAR USARIO NA SESSAO NO FILTER
			FacesContext context = FacesContext.getCurrentInstance();
			ExternalContext externalContext = context.getExternalContext();
			externalContext.getSessionMap().put("usuarioLogado", pessoaUser);
			return "PrimeiraPagina.jsf";
		}
		return "index.jsf";
	}

	public boolean permiteAcesso(String acesso) {
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		Pessoa pessoaUser = (Pessoa) externalContext.getSessionMap().get("usuarioLogado");

		return pessoaUser.getPerfilUser().equals(acesso);
	}

	public void pesquisaCep(AjaxBehaviorEvent event) { // METEDO PARA CEP WEBSERVICE
		try {
			URL url = new URL("https://viacep.com.br/ws/" + pessoa.getCep() + "/json/");
			URLConnection connection = url.openConnection();
			InputStream is = connection.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			String cep = "";
			StringBuilder jsonCep = new StringBuilder();
			while ((cep = br.readLine()) != null) {
				jsonCep.append(cep);
			}
			Pessoa gson = new Gson().fromJson(jsonCep.toString(), Pessoa.class);

			pessoa.setCep(gson.getCep());
			pessoa.setLogradouro(gson.getLogradouro());
			pessoa.setComplemento(gson.getComplemento());
			pessoa.setBairro(gson.getBairro());
			pessoa.setLocalidade(gson.getLocalidade());
			pessoa.setUf(gson.getUf());
			pessoa.setUnidade(gson.getUnidade());
			pessoa.setIbge(gson.getIbge());
			pessoa.setGia(gson.getGia());

		} catch (Exception e) {
			e.printStackTrace();
			mostrarMsg("erro ao consultar o CEP");
		}
	}

	public String deslogar() { // METEDO DESLOGAR
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		externalContext.getSessionMap().remove("usuarioLogado");

		HttpServletRequest httpServletRequest = (HttpServletRequest) context.getCurrentInstance().getExternalContext()
				.getRequest();
		httpServletRequest.getSession().invalidate();
		return "index.jsf";
	}

	public void carregaCidades(AjaxBehaviorEvent event) { // METEDO CARREGA CIDADES

		Estados estados = (Estados) ((HtmlSelectOneMenu) event.getSource()).getValue();

		if (estados != null) {
			pessoa.setEstados(estados);
			List<Cidades> cidades = JPAUtil.getEntityManager()
					.createQuery("from Cidades where estados.id = " + estados.getId()).getResultList();

			List<SelectItem> itemsCidades = new ArrayList<SelectItem>();
			for (Cidades cidade : cidades) {
				itemsCidades.add(new SelectItem(cidade, cidade.getNome()));
			}
			setCidades(itemsCidades);

		}
	}

	@SuppressWarnings("unchecked")
	public void editar() {
		if (pessoa.getCidades() != null) {
			Estados estados = pessoa.getCidades().getEstados();
			pessoa.setEstados(estados);

			List<Cidades> cidades = JPAUtil.getEntityManager()
					.createQuery("from Cidades where estados.id = " + estados.getId()).getResultList();

			List<SelectItem> itemsCidades = new ArrayList<SelectItem>();
			for (Cidades cidade : cidades) {
				itemsCidades.add(new SelectItem(cidade, cidade.getNome()));
			}
			setCidades(itemsCidades);
		}
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public DaoGeneric<Pessoa> getDaoGeneric() {
		return daoGeneric;
	}

	public void setDaoGeneric(DaoGeneric<Pessoa> daoGeneric) {
		this.daoGeneric = daoGeneric;
	}

	public List<Pessoa> getPessoas() {
		return pessoas;
	}

	public void setPessoas(List<Pessoa> pessoas) {
		this.pessoas = pessoas;
	}

	public List<SelectItem> getEstados() {
		estados = iDaoPessoa.listaEstados();
		return estados;
	}

	public void setEstados(List<SelectItem> estados) {
		this.estados = estados;
	}

	public List<SelectItem> getCidades() {

		return cidades;
	}

	public void setCidades(List<SelectItem> cidades) {
		this.cidades = cidades;
	}

	public IDaoPessoa getiDaoPessoa() {
		return iDaoPessoa;
	}

	public void setiDaoPessoa(IDaoPessoa iDaoPessoa) {
		this.iDaoPessoa = iDaoPessoa;
	}

	public Part getArquivofoto() {
		return arquivofoto;
	}

	public void setArquivofoto(Part arquivofoto) {
		this.arquivofoto = arquivofoto;
	}

	//METEDO CONVERTE UMA FOTO PARA BASE164
	@SuppressWarnings("unused")
	private byte[] getByte(InputStream is) throws IOException {

		int len;
		int size = 1024;
		byte[] buf = null;

		if (is instanceof ByteArrayInputStream) {
			size = is.available();
			buf = new byte[size];
			len = is.read(buf, 0, size);
		} else {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			buf = new byte[size];

			while ((len = is.read(buf, 0, size)) != -1) {
				bos.write(buf, 0, len);
			}
			buf = bos.toByteArray();
		}
		return buf;
	}
	public void baixarFoto() throws IOException {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String filebaixarId = params.get("filebaixarId");
		
		Pessoa pessoa = daoGeneric.consultar(Pessoa.class, filebaixarId);
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.addHeader("Content-Disposition", "attachment; filemane=download." + pessoa.getExtensao());
		response.setContentType("application/octet-stream");
		response.setContentLength(pessoa.getFotoIconBase64Original().length);
		response.getOutputStream().write(pessoa.getFotoIconBase64Original());
		response.getOutputStream().flush();
		FacesContext.getCurrentInstance().responseComplete();
	}

}
