package br.com.cursojsf;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

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
	private DaoGeneric<Pessoa> daoGeneric = new DaoGeneric<>();
	private List<Pessoa> pessoas = new ArrayList<>();
	
	private IDaoPessoa iDaoPessoa = new IDaoPessoaImpl();
	
	private List<SelectItem> estados;
	private List<SelectItem> cidades;

	public String salvar() {

		pessoa = daoGeneric.merge(pessoa);
		carregarPessoas();
		
		mostrarMsg("Registro salvo com sucesso!");
		
		return "";

	}

	private void mostrarMsg(String msg) {
		
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage(msg);
		
		context.addMessage(null, message); //Se eu não for enviar a mensagem para um componente deixo o primeiro parametro null
		
	}

	public String novo() {
		pessoa = new Pessoa();
		return "";
	}
	
	public String limpar() {
		
		pessoa = new Pessoa();
		return "";
	}

	public Pessoa getPessoa() {
		return pessoa;
	}
	
	public String remove() {
		daoGeneric.deletePorId(pessoa);
		pessoa = new Pessoa();
		carregarPessoas();
		
		mostrarMsg("Registro removido com sucesso!");
		
		return "";
	}
	
	public void pesquisaCep(AjaxBehaviorEvent event) {
		
		try {
			
			URL url = new URL("https://viacep.com.br/ws/" + pessoa.getCep() + "/json/"); //PEGA O CEP DO FRONT E MONTA O LINK
			URLConnection connection = url.openConnection(); // ABRE A CONEXÃO COM O LINK ACIMA
			InputStream is = connection.getInputStream();//EXECUTA A CONEXÃO
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			
			String cep = "";
			StringBuilder jsonCep = new StringBuilder();
			
			while ((cep = br.readLine()) != null) {
				
				jsonCep.append(cep);
			}
			
			Pessoa gsonAux = new Gson().fromJson(jsonCep.toString(), Pessoa.class);
			
			pessoa.setCep(gsonAux.getCep());
			pessoa.setLogradouro(gsonAux.getLogradouro());
			pessoa.setComplemento(gsonAux.getComplemento());
			pessoa.setBairro(gsonAux.getBairro());
			pessoa.setLocalidade(gsonAux.getLocalidade());
			pessoa.setUf(gsonAux.getUf());
			pessoa.setUnidade(gsonAux.getUnidade());
			pessoa.setIbge(gsonAux.getIbge());
			pessoa.setGia(gsonAux.getGia());
			
			
		} catch (Exception e) {
			e.printStackTrace();
			mostrarMsg("Erro ao consultar o CEP: " + pessoa.getCep());
		}
		
	}

	
	/*------------------------------------------------------------------------------------------------------------------------------*/
	@PostConstruct
	public void carregarPessoas() {
		pessoas = daoGeneric.getListEntity(Pessoa.class);
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
	
	
	/*Ao criar o metodo getEstados criei a linha estados = iDaoPessoa.listaEstado() e no JSF aponto para a variavel estado*/
	public List<SelectItem> getEstados() {
		
		estados = iDaoPessoa.lisstaEstados();
		
		return estados;
	}
	
/*------------------------------------------------------------------------------------------------------------------------------*/	
	
	
	public void carregaCidades(AjaxBehaviorEvent event) {
		
//		String codigoEstado = (String) event.getComponent().getAttributes().get("submittedValue");
		
		
		Estados estado = (Estados) ((HtmlSelectOneMenu) event.getSource()).getValue();
			
			
			if(estados != null) {
				
				pessoa.setEstados(estado);
				 
				List<Cidades> cidades = JPAUtil.getEntityManager()
						.createQuery("from Cidades where estados.id = " + estado.getId())
						.getResultList();
				
				List<SelectItem> selectItemsCidade = new ArrayList<>();
				
				for (Cidades cidade : cidades) {
					selectItemsCidade.add(new SelectItem(cidade, cidade.getNome()));
				}
				
				setCidades(selectItemsCidade);
			}
		
	}
	
	
	public void editar() {


		if(pessoa.getCidade() != null) {
			Estados estado = pessoa.getCidade().getEstados();
			pessoa.setEstados(estado);
			
			List<Cidades> cidades = JPAUtil.getEntityManager()
					.createQuery("from Cidades where estados.id = " + estado.getId())
					.getResultList();
			
			List<SelectItem> selectItemsCidade = new ArrayList<>();
			
			for (Cidades cidade : cidades) {
				selectItemsCidade.add(new SelectItem(cidade, cidade.getNome()));
			}
			
			setCidades(selectItemsCidade);
		
		}
	}
	
	public List<SelectItem> getCidades() {
		return cidades;
	}
	
	
	public void setCidades(List<SelectItem> cidades) {
		this.cidades = cidades;
	}
	
	public String logar() {
		
		Pessoa pessoaUser = iDaoPessoa.consultarUsuario(pessoa.getLogin(), pessoa.getSenha());
		
		if(pessoaUser != null) {//achou usuario
			
			//Adicionar usuário na sessão usuarioLogado
			FacesContext context = FacesContext.getCurrentInstance();
			ExternalContext externalContext = context.getExternalContext();
			externalContext.getSessionMap().put("usuarioLogado", pessoaUser);
			
			
			return "primeirapagina.jsf";
		}
		
		return "index.jsf";
	}
	
	
	public String deslogar() {
		
		/*Retiro a Key do usuarioLogado*/
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		externalContext.getSessionMap().remove("usuarioLogado");
		
		/*Entro na sessão Servlet e removo a sessão*/
		HttpServletRequest httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		httpServletRequest.getSession().invalidate();
		
		return "index.jsf";
	}
	
	public boolean permiteAcesso(String acesso) {
		
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		Pessoa pessoaUser = (Pessoa) externalContext.getSessionMap().get("usuarioLogado");
		
		return pessoaUser.getPerfilUser().equals(acesso);
		
	}
	

}
