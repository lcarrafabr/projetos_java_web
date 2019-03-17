package manegedBean;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import dao.DaoTelefones;
import dao.DaoUsuario;
import model.TelefoneUser;
import model.UsuarioPessoa;

@ManagedBean(name = "telefoneManagedBean")
@ViewScoped
public class TelefoneManagedBean {
	
	private UsuarioPessoa user = new UsuarioPessoa();
	private DaoUsuario<UsuarioPessoa> daoUser = new DaoUsuario<>();
	private DaoTelefones<TelefoneUser> daoTelefone = new DaoTelefones<>();
	
	private TelefoneUser telefone = new TelefoneUser();
	
	@PostConstruct
	public void init() {
		
		/*Essa linha pega o parametro enviado*/
		String coduser = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("codigouser");
		
		user = daoUser.pesquisar(Integer.parseInt(coduser), UsuarioPessoa.class);

	}
	
	public String salvar() {
		
		telefone.setUsuarioPessoa(user);
		daoTelefone.salvar(telefone);
		telefone = new TelefoneUser();
		user = daoUser.pesquisar(user.getId(), UsuarioPessoa.class);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação:", "Salvo com sucesso"));
		
		return "";
		
	}
	
	public String removeTelefone() throws Exception{
		
		daoTelefone.deletelarPorId(telefone);
		user = daoUser.pesquisar(user.getId(), UsuarioPessoa.class);
		telefone = new TelefoneUser();
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação:", "Removido com sucesso"));
		
		return "";
	}
	
	
	
	public UsuarioPessoa getUser() {
		return user;
	}
	
	public void setUser(UsuarioPessoa user) {
		this.user = user;
	}
	
	public void setDaoTelefone(DaoTelefones<TelefoneUser> daoTelefone) {
		this.daoTelefone = daoTelefone;
	}
	
	public DaoTelefones<TelefoneUser> getDaoTelefone() {
		return daoTelefone;
	}
	
	public TelefoneUser getTelefone() {
		return telefone;
	}
	
	public void setTelefone(TelefoneUser telefone) {
		this.telefone = telefone;
	}

}
