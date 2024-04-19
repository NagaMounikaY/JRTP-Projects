package in.ashokit.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import in.ashokit.dto.Dashboard;
import in.ashokit.entity.Counselleor;
import in.ashokit.entity.Enquiry;
import in.ashokit.repo.CounsellorRepo;
import in.ashokit.repo.EnquiryRepo;
@Service
public class EnquiryServiceImpl implements EnquiryService {
	@Autowired
	private EnquiryRepo erepo;
	
	@Autowired
	private CounsellorRepo crepo;
	

	@Override
	public Dashboard getDashboardInfo(Integer counsellorId) {
		Long totalEnq = erepo.getEnquires(counsellorId);
		Long openCnt = erepo.getEnquires(counsellorId,"new");
		Long lostEnq = erepo.getEnquires(counsellorId,"lost");
		Long enrolledCnt = erepo.getEnquires(counsellorId,"enrolled");
		
		Dashboard d=new Dashboard();
		d.setTotalEnqs(totalEnq);
		d.setEnrolledEnqs(enrolledCnt);
		d.setLostEnqs(lostEnq);
		d.setOpenEnqs(enrolledCnt);
		
		return d;
	}

	@Override
	public boolean addEnquiry(Enquiry enquiry, Integer counsellorId) {
		
		Counselleor counselleor = crepo.findById(counsellorId).orElseThrow();
		enquiry.setCounsellor(counselleor);//association for fk
		Enquiry save = erepo.save(enquiry);
		return save.getEnqId()!=null;
	}
	//viwes+filter
	@Override
	public List<Enquiry> getEnquiries(Enquiry enquiry, Integer counsellorId) {
		
		//Counselleor counselleor = crepo.findById(counsellorId).orElseThrow();
		Counselleor counselleor = new Counselleor();
		counselleor.setCounsellorId(counsellorId);
		
		Enquiry searchCriteria=new Enquiry();
		searchCriteria.setCounsellor(counselleor);
		
		if(null!=enquiry.getCourse() && ! "".equals(enquiry.getCourse())) {
			searchCriteria.setCourse(enquiry.getCourse());
		}
		
		if(null!=enquiry.getMode() && ! "".equals(enquiry.getMode())) {
			searchCriteria.setMode(enquiry.getMode());
		}
		
		if(null!=enquiry.getStatus() && ! "".equals(enquiry.getStatus())) {
			searchCriteria.setStatus(enquiry.getStatus());
		}
		
		//dynamic query creation
		Example<Enquiry> of = Example.of(searchCriteria);
		return erepo.findAll(of);
	}
	//edit
	@Override
	public Enquiry getEnquiry(Integer enqId) {
		Optional<Enquiry> id = erepo.findById(enqId);
		Enquiry enquiry=null;
		if(id.isPresent()) {
			enquiry=id.get();
		}
		return enquiry;
		
	}

}
