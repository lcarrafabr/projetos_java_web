package br.com.cursojsf;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import br.com.dao.DaoGeneric;
import br.com.entidades.Lancamento;
import br.com.entidades.Pessoa;
import br.com.repository.IDaoLancamento;
import br.com.repository.IDaoLancamentoImpl;

@ViewScoped
@ManagedBean(name = "lancamentoBean")
public class LancamentoBean {

	/* Instancia a classe */
	private Lancamento lancamento = new Lancamento();

	/* Instancia o DAOGeneric */
	private DaoGeneric<Lancamento> daoGeneric = new DaoGeneric<Lancamento>();
	private List<Lancamento> lancamentos = new ArrayList<>();
	
	private IDaoLancamento daoLancamento  = new IDaoLancamentoImpl();
	
	public String salvar() {
		
		/*Recupera a pessoa logada*/
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		Pessoa pessoaUser = (Pessoa) externalContext.getSessionMap().get("usuarioLogado");
		
		
		lancamento.setUsuario(pessoaUser);//Seta a pessoa logada
		lancamento = daoGeneric.merge(lancamento); //Salva os dados  ---------------------- PS usar o merge pois esse mesmo comando salva e atualiza
		
		carregarLancamentos();
		
		return "";
	}
	
	
	@PostConstruct  // INVOCA ESSE METODO ASSIM QUE A JANELA FOR ABERTA  <<<<<<<<<<<===============================
	private void carregarLancamentos() {
		
		/*Recupera a pessoa logada*/
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		Pessoa pessoaUser = (Pessoa) externalContext.getSessionMap().get("usuarioLogado");
		
		
		/*Carregar os lançamentos apenas do usuário logado*/
		lancamentos = daoLancamento.consultar(pessoaUser.getId());
	}

	public String novo() {
		
		lancamento = new Lancamento();
		
		return "";
	}
	
	public String remover() {
		
		daoGeneric.deletePorId(lancamento);
		lancamento = new Lancamento(); // limpa o objeto
		carregarLancamentos();
		
		return "";
	}
	
	
	
	/*Getters and Setters do Lancamento
	 *Getters and Setters do DAOGeneric
	 *Getters and Setters do ArrayList */
	public Lancamento getLancamento() {
		return lancamento;
	}

	public void setLancamento(Lancamento lancamento) {
		this.lancamento = lancamento;
	}

	public DaoGeneric<Lancamento> getDaoGeneric() {
		return daoGeneric;
	}

	public void setDaoGeneric(DaoGeneric<Lancamento> daoGeneric) {
		this.daoGeneric = daoGeneric;
	}

	public List<Lancamento> getLancamentos() {
		return lancamentos;
	}

	public void setLancamentos(List<Lancamento> lancamentos) {
		this.lancamentos = lancamentos;
	}

}
