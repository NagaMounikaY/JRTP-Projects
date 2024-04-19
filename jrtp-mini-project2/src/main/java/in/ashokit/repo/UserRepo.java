package in.ashokit.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import in.ashokit.entity.UserDtlEntity;

public interface UserRepo extends JpaRepository<UserDtlEntity,Integer>{
	
	public UserDtlEntity findByEmail(String email);

	public UserDtlEntity findByEmailAndPwd(String email, String pwd);

}
