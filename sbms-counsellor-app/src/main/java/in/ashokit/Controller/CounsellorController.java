package in.ashokit.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import in.ashokit.dto.Dashboard;
import in.ashokit.entity.Counselleor;
import in.ashokit.service.CounsellorService;
import in.ashokit.service.EnquiryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
@Controller
public class CounsellorController {
	@Autowired
	private CounsellorService cservice;
	
	@Autowired
	private EnquiryService eservice;
	
	@GetMapping("/logout")
	public String logout(HttpServletRequest req, Model model) {
		HttpSession session = req.getSession(false);
		session.invalidate();
		model.addAttribute("counsellor",new Counselleor());
		return "index";
	}
	
	@GetMapping("/register")
	public String register(Model model) {
		model.addAttribute("counsellor", new Counselleor());
		return "registerView";
	}
	
	@PostMapping("/register")
	public String handleRegister(Counselleor c, Model model) {
		boolean status = cservice.saveCounsellor(c);
		if(status) {
			model.addAttribute("smsg","counsellor saved..");
		}else {
			model.addAttribute("emsg","failed to save..");
		}
		model.addAttribute("counsellor", new Counselleor());
		return "registerView";
	}
	
	
	@GetMapping("/login")
	public String login(Model model) {
		model.addAttribute("counsellor",new Counselleor());
		return "index";
	}
	
	@PostMapping("/login")
	public String handleLogin(Counselleor counsellor,HttpServletRequest req, Model model) {
		Counselleor c = cservice.getCounsellor(counsellor.getEmail(),counsellor.getPwd());
		if(c == null) {
			model.addAttribute("emsg","Invalid credentials..");
			model.addAttribute("counsellor",new Counselleor());
			return "index";
		}else {
			//set counsellor session id
			HttpSession session = req.getSession(true);
			session.setAttribute("cid",c.getCounsellorId());
			
			Dashboard info = eservice.getDashboardInfo(c.getCounsellorId());
			model.addAttribute("dashboard",info);
			return "dashboard";
		}
	}
	
	@GetMapping("/dashboard")
	public String buildDashboard(HttpServletRequest req, Model model) {
		
		HttpSession session = req.getSession(true);
		Integer cid =(Integer) session.getAttribute("cid");
		
		Dashboard info = eservice.getDashboardInfo(cid);
		model.addAttribute("dashboard",info);
		return "dashboard";
	}

}