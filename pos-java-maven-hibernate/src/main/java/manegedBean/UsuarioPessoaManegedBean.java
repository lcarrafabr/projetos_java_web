package manegedBean;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import dao.DaoUsuario;
import model.UsuarioPessoa;

@ManagedBean(name = "usuarioPessoaManegedBean")
@ViewScoped
public class UsuarioPessoaManegedBean {

	private UsuarioPessoa usuarioPessoa = new UsuarioPessoa();
	
	private List<UsuarioPessoa> list = new ArrayList<>(); //usado para carregar a lista de pessoas que irá aparecer na table
	private DaoUsuario<UsuarioPessoa> daoGeneric = new DaoUsuario<>();

	
	@PostConstruct //Depois que o ManegedBean é executado ele roda esse método uma vez. Para isso precisa do EqualHashCode na model
	public void init() {
		
		list = daoGeneric.listar(UsuarioPessoa.class);
		
	}
	
	public String novo() {
		usuarioPessoa = new UsuarioPessoa();
		return "";
	}
	
	
	public String remover() {
		
		try {
//			daoGeneric.deletelarPorId(usuarioPessoa); /*Esta linha remove mas não em cascata*/
			
			daoGeneric.removerUsuario(usuarioPessoa); //Usa o remover em cascata
			
			list.remove(usuarioPessoa); //remover o usuario da lista
			
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação:", "Removido com sucesso"));
			usuarioPessoa = new UsuarioPessoa();
			
		} catch (Exception e) {
			if(e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Informação:", "Existem telefones para o usuário"));
			}
		}
		return "";
	}
	
	
	public UsuarioPessoa getUsuarioPessoa() {
		return usuarioPessoa;
	}

	public void setUsuarioPessoa(UsuarioPessoa usuarioPessoa) {
		this.usuarioPessoa = usuarioPessoa;
	}
	public String salvar() {
		
		daoGeneric.salvar(usuarioPessoa);
		list.add(usuarioPessoa); //Quando salvar, inclua o usuário na lista
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação:", "Salvo com sucesso"));
//		novo(); /*Caso queira que após salvar o formulário fique em branco*/
		return "";
	}
	
	
	public List<UsuarioPessoa> getList() {
//		list = daoGeneric.listar(UsuarioPessoa.class);//Carrega a lista de pessoas ao chaamr o getList
		return list;
	}

}
