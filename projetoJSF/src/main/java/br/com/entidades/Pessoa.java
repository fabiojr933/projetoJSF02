package br.com.entidades;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
public class Pessoa implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String nome;
	private String sobreNome;
	private String idade;
	private String sexo;
	private String nivelProgramador;
	private String[] frameworks;
	private Boolean ativo;
	private String login;
	private String senha;
	private String perfilUser;
	private String[] linguagem;
	@Temporal(TemporalType.DATE)
	private Date dataNascimento = new Date();;
	private String cep;
	private String logradouro;
	private String complemento;
	private String bairro;
	private String localidade;
	private String uf;
	private String unidade;
	private String ibge;
	private String gia;
	@ManyToOne
	private Cidades cidades;

	@Transient //FICA SO NA MENORIA NAO GRAVA NO BANCO
	private Estados estados;
	
	@Column(columnDefinition = "text")
	private String fotoIconBase64;		
	private String extensao;
	
	@Lob  //GRAVAR ARQUIVOS NO BANCO
	@Basic(fetch = FetchType.LAZY)
	private byte[] fotoIconBase64Original;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSobreNome() {
		return sobreNome;
	}

	public void setSobreNome(String sobreNome) {
		this.sobreNome = sobreNome;
	}

	public String getIdade() {
		return idade;
	}

	public void setIdade(String idade) {
		this.idade = idade;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public String[] getFrameworks() {
		return frameworks;
	}

	public void setFrameworks(String[] frameworks) {
		this.frameworks = frameworks;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getPerfilUser() {
		return perfilUser;
	}

	public void setPerfilUser(String perfilUser) {
		this.perfilUser = perfilUser;
	}

	public String getNivelProgramador() {
		return nivelProgramador;
	}

	public void setNivelProgramador(String nivelProgramador) {
		this.nivelProgramador = nivelProgramador;
	}

	public String[] getLinguagem() {
		return linguagem;
	}

	public void setLinguagem(String[] linguagem) {
		this.linguagem = linguagem;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}


	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getLocalidade() {
		return localidade;
	}

	public void setLocalidade(String localidade) {
		this.localidade = localidade;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getUnidade() {
		return unidade;
	}

	public void setUnidade(String unidade) {
		this.unidade = unidade;
	}

	public String getIbge() {
		return ibge;
	}

	public void setIbge(String ibge) {
		this.ibge = ibge;
	}

	public String getGia() {
		return gia;
	}

	public void setGia(String gia) {
		this.gia = gia;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public Estados getEstados() {
		return estados;
	}

	public void setEstados(Estados estados) {
		this.estados = estados;
	}

	public Cidades getCidades() {
		return cidades;
	}

	public void setCidades(Cidades cidades) {
		this.cidades = cidades;
	}

	public String getFotoIconBase64() {
		return fotoIconBase64;
	}

	public void setFotoIconBase64(String fotoIconBase64) {
		this.fotoIconBase64 = fotoIconBase64;
	}

	public String getExtensao() {
		return extensao;
	}

	public void setExtensao(String extensao) {
		this.extensao = extensao;
	}

	public byte[] getFotoIconBase64Original() {
		return fotoIconBase64Original;
	}

	public void setFotoIconBase64Original(byte[] fotoIconBase64Original) {
		this.fotoIconBase64Original = fotoIconBase64Original;
	}

	
}
