package posjavamavenhibernate;

import java.util.List;

import org.junit.Test;

import dao.DaoGeneric;
import model.TelefoneUser;
import model.UsuarioPessoa;

public class TesteHibernate {

	@Test
	public void testeHibernateUtil() {

//		HibernateUtil.getEntityManager();// testa a criação de tabela e conexão com o banco.

		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<>();

		UsuarioPessoa pessoa = new UsuarioPessoa();

		pessoa.setEmail("teste@teste.copm");
		pessoa.setLogin("teste 3");
		pessoa.setNome("teste 3");
		pessoa.setSobrenome("teste 3");
		pessoa.setSenha("123");
		pessoa.setIdade(29);

		daoGeneric.salvar(pessoa);

	}

	@Test
	public void testeBuscar() {

		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<>();
		
		UsuarioPessoa pessoa = daoGeneric.pesquisar(1 , UsuarioPessoa.class);
		
		System.out.println(pessoa);

	}
	
	@Test
	public void testeUpdate() {

		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<>();

		UsuarioPessoa pessoa = daoGeneric.pesquisar(10 , UsuarioPessoa.class);
		
		pessoa.setIdade(110);
		pessoa.setNome("Teste atualizar Hibernate");
//		pessoa.setSenha("123");
		
		pessoa = daoGeneric.updateMerge(pessoa);
		
//		System.out.println(pessoa);

	}
	
	@Test
	public void testeDelete() {

		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<>();

		UsuarioPessoa pessoa = daoGeneric.pesquisar(13 , UsuarioPessoa.class);
		
//		daoGeneric.deletelarPorId(pessoa);
	
//		System.out.println(pessoa);
	}
	
	@Test
	public void testeConsultar() {

		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<>();

		List<UsuarioPessoa> list = daoGeneric.listar(UsuarioPessoa.class);
		
		for (UsuarioPessoa usuarioPessoa : list) {
			System.out.println(usuarioPessoa);
			System.out.println("----------------------------------------------------------------------------");
		}
	}
	
	@Test
	public void testeQueryList() {
		
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<>();
		
		List<UsuarioPessoa> list = daoGeneric.getEntityManager().createNamedQuery("from UsuarioPessoa").getResultList();
		
		for (UsuarioPessoa usuarioPessoa : list) {
			System.out.println(usuarioPessoa);
		}
	}
	
	@Test
	public void testeQueryListMaxResult() {
		
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<>();
		
		List<UsuarioPessoa> list = daoGeneric.getEntityManager()
				.createQuery(" from UsuarioPessoa order by id")
				.setMaxResults(5)
				.getResultList();
		
		for (UsuarioPessoa usuarioPessoa : list) {
//			System.out.println(usuarioPessoa);
		}
	}
	
	@Test
	public void testeQueryListParametro() {
		
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<>();
		
		List<UsuarioPessoa> list = daoGeneric.getEntityManager()
				.createQuery(" from UsuarioPessoa where nome = :nome")
				.setParameter("nome", "Débora").getResultList();
		
		for (UsuarioPessoa usuarioPessoa : list) {
			
//			System.out.println(usuarioPessoa);
			
		}
	}
	
	@Test
	public void testeQuerySomaIdade() {
		
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<>();
		
//		Long somaIdade = (Long) daoGeneric.getEntityManager()
//				.createQuery("select sum(u.idade) from UsuarioPessoa u ").getSingleResult();
		
		Double somaIdade = (Double) daoGeneric.getEntityManager()
				.createQuery("select avg(u.idade) from UsuarioPessoa u ").getSingleResult();
		
//		System.out.println("A média de todas as idades é: " + somaIdade);
	}
	
	@Test
	public void testeNamedQuery() {
		
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<>();
		
		List<UsuarioPessoa> list = daoGeneric.getEntityManager().createNamedQuery("UsuarioPessoa.findAll").getResultList();
		
		for (UsuarioPessoa usuarioPessoa : list) {
			
//			System.out.println(usuarioPessoa);
		}
	}
	
	@Test
	public void testeNamedQuery2() {
		
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<>();
		
		List<UsuarioPessoa> list = daoGeneric.getEntityManager()
				.createNamedQuery("UsuarioPessoa.buscaPorNome").setParameter("nome", "Débora")
				.getResultList();
		
		for (UsuarioPessoa usuarioPessoa : list) {
			
//			System.out.println(usuarioPessoa);
		}
	}
	
	@Test
	public void testeGravaTelefone() {
		
		DaoGeneric daoGeneric = new DaoGeneric();
		
		UsuarioPessoa pessoa = (UsuarioPessoa) daoGeneric.pesquisar(1, UsuarioPessoa.class);
		
		TelefoneUser telefoneUser = new TelefoneUser();
		
		telefoneUser.setNumero("(11) 2254-7752");
		telefoneUser.setTipoTelefone("Empresa");
		telefoneUser.setUsuarioPessoa(pessoa);
		
		daoGeneric.salvar(telefoneUser);
		
	}
	
	@Test
	public void testeConsultaTelefones() {
		
		DaoGeneric daoGeneric = new DaoGeneric();
		
		UsuarioPessoa pessoa = (UsuarioPessoa) daoGeneric.pesquisar(1, UsuarioPessoa.class);
		
		for (TelefoneUser fone : pessoa.getTelefoneUsers()) {
			System.out.println(fone.getNumero());
			System.out.println(fone.getTipoTelefone());
			System.out.println(fone.getUsuarioPessoa().getNome());
			System.out.println("------------------------------------------------------------------------------");
		}
		
	}

}
