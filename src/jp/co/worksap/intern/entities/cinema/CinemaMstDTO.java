package jp.co.worksap.intern.entities.cinema;

import jp.co.worksap.intern.entities.ICsvMasterDTO;

public class CinemaMstDTO implements ICsvMasterDTO {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3711231132225687846L;
	private Long cinemaId;
	private Long regionId;
	private String cinemaName;
	private String address;
	private String tel;

	/**
	 * Default Constructor 
	 * 
	 * @param cinemaId
	 * @param cinemaName
	 * @param address
	 * @param tel
	 */
	public CinemaMstDTO(Long cinemaId, Long regionId, String cinemaName, String address,
			String tel) {
		super();
		this.cinemaId = cinemaId;
		this.regionId = regionId;
		this.cinemaName = cinemaName;
		this.address = address;
		this.tel = tel;
	}

	public Long getCinemaId() {
		return cinemaId;
	}

	public Long getRegionId() {
		return regionId;
	}

	public String getCinemaName() {
		return cinemaName;
	}

	public String getAddress() {
		return address;
	}

	public String getTel() {
		return tel;
	}

}
