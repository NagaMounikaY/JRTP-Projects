package in.ashokit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.ashokit.entity.Counselleor;
import in.ashokit.repo.CounsellorRepo;
@Service
public class CounsellorServiceImpl implements CounsellorService {
	@Autowired
	private CounsellorRepo crepo;
	@Override
	public boolean saveCounsellor(Counselleor counsellor) {
		Counselleor email = crepo.findByEmail(counsellor.getEmail());
		if(email!=null) {
			return false;
		}else {
			Counselleor save = crepo.save(counsellor);
			return save.getCounsellorId()!=null;
			
		}
	}

	@Override
	public Counselleor getCounsellor(String email,String pwd) {
		return crepo.findByEmailAndPwd(email,pwd);
		
	}

}
