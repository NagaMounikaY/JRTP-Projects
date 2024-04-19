package in.ashokit.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import in.ashokit.dto.LoginDto;
import in.ashokit.dto.RegisterDto;
import in.ashokit.dto.ResetPwdDto;
import in.ashokit.dto.UserDto;
import in.ashokit.service.UserService;
import in.ashokit.utils.AppConstants;
import in.ashokit.utils.AppProperties;

@Controller
public class UserController {
	
	@Autowired
	private UserService service;
	@Autowired
	private AppProperties props;
	
	@GetMapping("/register")
	public String registerPage(Model model) {
		model.addAttribute("registerDto", new RegisterDto());
		Map<Integer, String> countriesMap = service.getCountries();
		model.addAttribute("countries", countriesMap);
		
		return "registerView";
	}
	
	@GetMapping("/states/{cid}")
	@ResponseBody
	public Map<Integer, String> getStates(@PathVariable("cid") Integer cid){
		
		return service.getStates(cid);
	}

	@GetMapping("cities/{sid}")
	@ResponseBody
	public Map<Integer, String> getCities(@PathVariable("sid") Integer sid){
		
		return service.getCities(sid);
	}

	@PostMapping("/register")
	public String register(RegisterDto regDto, Model model) {
		
		model.addAttribute("countries", service.getCountries());
		
		Map<String,String> message = props.getMessage();
		UserDto user = service.getUser(regDto.getEmail());
		if (user != null) {
			model.addAttribute(AppConstants.ERROR_MSG, message.get("dupEmail"));
			return "registerView";
		}
		boolean registerUser = service.registerUser(regDto);
		if (registerUser) {
			model.addAttribute(AppConstants.SUCESS_MSG, message.get("regsucc"));
		} else {
			model.addAttribute(AppConstants.ERROR_MSG, message.get("regFail"));
		}
//		

		
		return "registerView";
	}

	@GetMapping("/")
	public String loginPage(Model model) {
		
		model.addAttribute("loginDto", new LoginDto());
		return "index";
	}

	@PostMapping("/login")
	public String login(LoginDto loginDto, Model model) {
		Map<String,String> message = props.getMessage();
		UserDto user = service.getUser(loginDto);
		if (user == null) {
			model.addAttribute(AppConstants.ERROR_MSG, message.get("InvalidCredentials"));
			return "index";
		}

		if ("YES".equals(user.getPwdUpdated())) {
			return "redirect:dashboard";
		} else {
			// pwd not uploaded
			ResetPwdDto resetPwdDto = new ResetPwdDto();
			resetPwdDto.setEmail(user.getEmail());
			model.addAttribute("resetPwdDto", resetPwdDto);
//			model.addAttribute("resetPwdDto", new ResetPwdDto());
			return AppConstants.RESETPWD_VIEW;
		}
		
	}

	@PostMapping("/resetPwd")
	public String resetPwd(ResetPwdDto pwdDto, Model model) {
		
		Map<String,String> message = props.getMessage();
		ResetPwdDto resetPwdDto = new ResetPwdDto();
		model.addAttribute("resetPwdDto", resetPwdDto);
		if ((!pwdDto.getNewPwd().equals(pwdDto.getConfirmPwd()))) {
			model.addAttribute(AppConstants.ERROR_MSG, message.get("PwdMatchErr"));
			return AppConstants.RESETPWD_VIEW;
		}

		UserDto user = service.getUser(pwdDto.getEmail());
		if (user.getPwd().equals(pwdDto.getOldPwd())) {

			boolean resetPwd = service.resetPwd(pwdDto);
			if (resetPwd) {
				return "redirect:dashboard";
			} else {
				model.addAttribute(AppConstants.ERROR_MSG, message.get("pwdUpdateErr"));
				return AppConstants.RESETPWD_VIEW;

			}
		} else {
			model.addAttribute(AppConstants.ERROR_MSG, message.get("oldPwdErr"));
			return AppConstants.RESETPWD_VIEW;
		}
		
	}

	@GetMapping("/dashboard")
	public String dashboard(Model model) {
		String quote = service.getQuote();
		model.addAttribute("quote", quote);
		
		return "dashboardView";
	}

	@GetMapping("/logout")
	public String logout() {
		
		return "redirect:/";
	}

	
	
	
	

}
