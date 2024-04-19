package in.ashokit.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class StateEntity {
	@Id
	private Integer stateId;
	private String stateName;
	
	@ManyToOne
	@JoinColumn(name="country_id")
	private CountryEntity Country;

	public Integer getStateId() {
		return stateId;
	}

	public void setStateId(Integer stateId) {
		this.stateId = stateId;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public CountryEntity getCountry() {
		return Country;
	}

	public void setCountry(CountryEntity country) {
		Country = country;
	}
	
	

}
