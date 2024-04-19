package in.ashokit.service;

import in.ashokit.entity.Counselleor;

public interface CounsellorService {
	public boolean saveCounsellor(Counselleor counsellor);
	public  Counselleor getCounsellor(String email, String pwd);

}
