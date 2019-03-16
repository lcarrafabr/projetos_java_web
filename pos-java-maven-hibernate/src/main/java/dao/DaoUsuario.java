package dao;

import model.UsuarioPessoa;

public class DaoUsuario<E> extends DaoGeneric<UsuarioPessoa>{
	
	public void removerUsuario(UsuarioPessoa pessoa) throws Exception{
		
		getEntityManager().getTransaction().begin();
		
		/*Aqui remove os filhos no caso os telefones que existir para esse usuário*/
		String sqlDeleteFone = "delete from telefoneuser where usuariopessoa_id = " + pessoa.getId();
		getEntityManager().createNativeQuery(sqlDeleteFone).executeUpdate();
		getEntityManager().getTransaction().commit();
		
		/*Aqui remove o pai depois de remover os telefones desse usuário (delete em cascata)*/
		super.deletelarPorId(pessoa);
		
	}

}
