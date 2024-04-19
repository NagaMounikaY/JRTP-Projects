package in.ashokit.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import in.ashokit.dto.LoginDto;
import in.ashokit.dto.QuoteDto;
import in.ashokit.dto.RegisterDto;
import in.ashokit.dto.ResetPwdDto;
import in.ashokit.dto.UserDto;
import in.ashokit.entity.CityEntity;
import in.ashokit.entity.CountryEntity;
import in.ashokit.entity.StateEntity;
import in.ashokit.entity.UserDtlEntity;
import in.ashokit.repo.CityRepo;
import in.ashokit.repo.CountryRepo;
import in.ashokit.repo.StateRepo;
import in.ashokit.repo.UserRepo;
import in.ashokit.utils.EmailUtils;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepo urepo;
	@Autowired
	private CountryRepo crepo;
	@Autowired
	private StateRepo srepo;
	@Autowired
	private CityRepo cityrepo;
	@Autowired
	private EmailUtils emailUtil;
	
	private QuoteDto[] quotations = null;

	@Override
	public Map<Integer, String> getCountries() {
		Map<Integer, String> countryMap=new HashMap<>();
		List<CountryEntity> all = crepo.findAll();
		all.forEach(c->{
			countryMap.put(c.getCountryId(),c.getCountryName());
		});
		return countryMap;
	}

	@Override
	public Map<Integer, String> getStates(Integer cid) {
		/*CountryEntity country=new CountryEntity();
		country.setCountryId(cid);
		
		StateEntity state=new StateEntity();
		state.setCountry(country);
		
		Example<StateEntity> of = Example.of(state);
		srepo.findAll(of);*/
		Map<Integer, String> statesMap = new HashMap<>();
		
		List<StateEntity> stateList = srepo.getStates(cid);
		stateList.forEach(s -> {
			statesMap.put(s.getStateId(), s.getStateName());
		});
		return statesMap;
	}

	@Override
	public Map<Integer, String> getCities(Integer sid) {
		
		Map<Integer, String> cityMap = new HashMap<>();

		List<CityEntity> cityList = cityrepo.getCities(sid);
		cityList.forEach(c -> {
			cityMap.put(c.getCityId(), c.getCityName());
		});
		return cityMap;
	}

	@Override
	public UserDto getUser(String email) {
		
		UserDtlEntity user = urepo.findByEmail(email);
//		 UserDto dto = new UserDto(); 
//		 BeanUtils.copyProperties(user, dto);
		if(user==null) {
			return null;
			
		}
		
		ModelMapper mapper=new ModelMapper();
		UserDto userDto = mapper.map(user, UserDto.class);

		return userDto;
	}

	@Override
	public boolean registerUser(RegisterDto regDto) {
		
		ModelMapper mapper = new ModelMapper();
		UserDtlEntity entity = mapper.map(regDto, UserDtlEntity.class);

		CountryEntity country = crepo.findById(regDto.getCountryId()).orElseThrow();

		StateEntity state = srepo.findById(regDto.getStateId()).orElseThrow();

		CityEntity city = cityrepo.findById(regDto.getCityId()).orElseThrow();

		entity.setCountry(country);
		entity.setState(state);
		entity.setCity(city);

		entity.setPwd(generateRandom());
		entity.setPwdUpdated("no");

		// user registration
		UserDtlEntity saveEntity = urepo.save(entity);
		String subject = "User Registration";

		String body = "Your temporary Pwd is " + entity.getPwd();
		emailUtil.sendEmail(regDto.getEmail(), subject, body);
		return saveEntity.getUserId() != null;
	}

	@Override
	public UserDto getUser(LoginDto loginDto) {
		
		UserDtlEntity user = urepo.findByEmailAndPwd(loginDto.getEmail(), loginDto.getPwd());

		if (user == null) {
			return null;
		}

		ModelMapper mapper = new ModelMapper();
		return mapper.map(user, UserDto.class);
	}

	@Override
	public boolean resetPwd(ResetPwdDto pwdDto) {
		
		UserDtlEntity user = urepo.findByEmailAndPwd(pwdDto.getEmail(), pwdDto.getOldPwd());

		if (user != null) {
			user.setPwd(pwdDto.getNewPwd());
			user.setPwdUpdated("YES");
			urepo.save(user);
			return true;
		}
		return false;
	}

	@Override
	public String getQuote() {
		
		if(quotations == null) {
			String url = "https://type.fit/api/quotes";

			// web service call
			RestTemplate rt = new RestTemplate();
			ResponseEntity<String> forEntity = rt.getForEntity(url, String.class);
			String responseBody = forEntity.getBody();
			ObjectMapper mapper = new ObjectMapper();

			try {
				quotations = mapper.readValue(responseBody, QuoteDto[].class);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		Random r= new Random();
		int index=r.nextInt(quotations.length-1);

		return quotations[index].getText();
	}
	
	private static String generateRandom() {
		String aToZ = "ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789";
		Random rand = new Random();
		StringBuilder res = new StringBuilder();
		for (int i = 0; i < 5; i++) {
			int randIndex = rand.nextInt(aToZ.length());
			res.append(aToZ.charAt(randIndex));
		}
		return res.toString();
	}

}
