package in.ashokit.service;

import java.util.List;

import in.ashokit.dto.Dashboard;
import in.ashokit.entity.Enquiry;

public interface EnquiryService {
	public Dashboard getDashboardInfo(Integer counsellorId);
	
	public boolean addEnquiry(Enquiry enquiry, Integer counsellorId);
	
	public List<Enquiry> getEnquiries(Enquiry enquiry, Integer counsellorId);
	
	public Enquiry getEnquiry(Integer enqId);

}
