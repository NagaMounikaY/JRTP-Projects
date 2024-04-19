package in.ashokit.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import in.ashokit.entity.Enquiry;
import in.ashokit.service.EnquiryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class EnquiryController {
	@Autowired
	private EnquiryService eservice;
	
	//add enquiry page display
	@GetMapping("/enquiry")
	public String addEnquiry(Model model) {
		model.addAttribute("enq", new Enquiry());
		return "addEnq";
	}
	
	//save enq
	@PostMapping("/enquiry")
	public String saveEnq(Enquiry enq, HttpServletRequest req, Model model) {
		HttpSession session = req.getSession(false);
		Integer cid = (Integer)session.getAttribute("cid");
		
		boolean status = eservice.addEnquiry(enq, cid);
		if(status) {
			model.addAttribute("smsg","Enquiry added..");
		}else {
			model.addAttribute("emsg","Enquity not saved..");
		}
		model.addAttribute("enq", new Enquiry());
		
		return "addEnq";
	}
	
	
	//view enq
	@GetMapping("/enquires")
	public String getEnq(Model model,HttpServletRequest req) {
		HttpSession session = req.getSession(false);
		Integer cid = (Integer)session.getAttribute("cid");
		
		List<Enquiry> list = eservice.getEnquiries(new Enquiry(), cid);
		model.addAttribute("enqs",list);
		model.addAttribute("enq",new Enquiry());
		return "viewEnquires";
	}
	
	//fiter enq
	@PostMapping("/filter-enqs")
	private String filterEnqs(@ModelAttribute("enq") Enquiry enq,HttpServletRequest req, Model model) {
		HttpSession session = req.getSession(false);
		Integer cid = (Integer)session.getAttribute("cid");
		
		model.addAttribute("enq", new Enquiry());
		
		List<Enquiry> list = eservice.getEnquiries(enq, cid);
		model.addAttribute("enqs",list);
		return "viewEnquiries";
		
	}
	
	//edit & update enq
	@GetMapping("/edit/{enqId}")
	public String editEnquiry(@RequestParam("id") Integer enqId, Model model) {
		Enquiry enquiry = eservice.getEnquiry(enqId);
		model.addAttribute("enq",enquiry);
		return "addEnq";
	}

}
