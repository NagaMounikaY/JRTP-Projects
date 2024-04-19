package in.ashokit.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import in.ashokit.entity.Counselleor;

public interface CounsellorRepo extends JpaRepository<Counselleor,Integer> {
	public Counselleor findByEmailAndPwd(String email,String pwd);
	
	public Counselleor findByEmail(String email);
	
}
