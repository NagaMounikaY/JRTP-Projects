package in.ashokit.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.ashokit.entity.Enquiry;

public interface EnquiryRepo extends JpaRepository<Enquiry,Integer> {
	
	@Query(value="select count(*) from enquiry where counsellor_id=:id",nativeQuery=true)
	public Long getEnquires(Integer id);
	
	@Query(value="select count(*) from enquiry where counsellor_id=:id and status=:status",nativeQuery=true)
	public Long  getEnquires(Integer id,String status);
	
	
	

}
