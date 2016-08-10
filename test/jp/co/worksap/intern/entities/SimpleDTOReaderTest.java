package jp.co.worksap.intern.entities;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.List;

import jp.co.worksap.intern.constants.Constants;
import jp.co.worksap.intern.entities.cinema.CinemaMstDTO;
import jp.co.worksap.intern.entities.cinema.CinemaMstDTOReader;
import jp.co.worksap.intern.entities.customer.CustomerDTO;
import jp.co.worksap.intern.entities.customer.CustomerDTOReader;
import jp.co.worksap.intern.entities.region.RegionMstDTO;
import jp.co.worksap.intern.entities.region.RegionMstDTOReader;
import jp.co.worksap.intern.entities.staff.StaffDTO;
import jp.co.worksap.intern.entities.staff.StaffDTOReader;

import org.junit.Test;

public class SimpleDTOReaderTest {

	@Test
	public void readCustomerTable() throws IOException {
		CustomerDTOReader reader = new CustomerDTOReader();
		List<CustomerDTO> values = reader.getValues();
		assertNotNull(values);
		for (CustomerDTO value : values) {
			assertNotNull(value);
			assertNotNull(value.getCustomerId());
			assertNotNull(value.getGender());
			assertNotNull(value.getName());
			assertNotNull(value.getTel());
		}
	}

	@Test
	public void readEmployeeTable() throws IOException {
		StaffDTOReader reader = new StaffDTOReader(Constants.DEFAULT_CSV_FOLDER
				+ "STAFF_MST.csv");
		List<StaffDTO> values = reader.getValues();
		assertNotNull(values);
		for (StaffDTO value : values) {
			assertNotNull(value);
			assertNotNull(value.getStaffId());
			assertNotNull(value.getGender());
			assertNotNull(value.getName());
			assertNotNull(value.getPosition());
			assertNotNull(value.getRank());
			assertNotNull(value.getCinemaId());
		}
	}

	@Test
	public void readCinemaMstTable() throws IOException {
		CinemaMstDTOReader reader = new CinemaMstDTOReader();
		List<CinemaMstDTO> values = reader.getValues();
		assertNotNull(values);
		for (CinemaMstDTO value : values) {
			assertNotNull(value);
			assertNotNull(value.getAddress());
			assertNotNull(value.getCinemaId());
			assertNotNull(value.getCinemaName());
			assertNotNull(value.getTel());
		}
	}

	@Test
	public void readRegionTable() throws IOException {
		RegionMstDTOReader reader = new RegionMstDTOReader();
		List<RegionMstDTO> values = reader.getValues();
		assertNotNull(values);
		for (RegionMstDTO value : values) {
			assertNotNull(value);
			assertNotNull(value.getManagerId());
			assertNotNull(value.getName());
			assertNotNull(value.getRegionId());
		}
	}
}
