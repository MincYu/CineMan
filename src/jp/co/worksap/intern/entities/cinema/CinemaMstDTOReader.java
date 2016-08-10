package jp.co.worksap.intern.entities.cinema;

import java.io.IOException;

import jp.co.worksap.intern.constants.Constants;
import jp.co.worksap.intern.entities.AbstractDTOReader;

public class CinemaMstDTOReader extends AbstractDTOReader<CinemaMstDTO> {
	private static final int COLUMN_INDEX_CINEMA_ID = 0;
	private static final int COLUMN_INDEX_REGION_ID = 1;
	private static final int COLUMN_INDEX_CINEMA_NAME = 2;
	private static final int COLUMN_INDEX_ADDRESS = 3;
	private static final int COLUMN_INDEX_TEL = 4;

	private String fileAddress = Constants.DEFAULT_CSV_FOLDER + "CINEMA_MST.csv";

	/**
	 * use default file address
	 * 
	 * @throws IOException
	 */
	public CinemaMstDTOReader() throws IOException {
		super(CinemaMstDTOReader.class.getName());
		super.init();
	}

	/**
	 * use customize file address
	 * 
	 * @param fileAddress
	 * @throws IOException
	 */
	public CinemaMstDTOReader(final String fileAddress) throws IOException {
		super(CinemaMstDTOReader.class.getName());
		this.fileAddress = fileAddress;
		super.init();
	}

	@Override
	protected String getFileArress() {
		return fileAddress;
	}

	@Override
	protected CinemaMstDTO convertArrayToDTO(String[] value) throws IOException {
		Long cinemaId = Long.valueOf(value[COLUMN_INDEX_CINEMA_ID]);
		Long regionId = Long.valueOf(value[COLUMN_INDEX_REGION_ID]);
		String cinemaName = value[COLUMN_INDEX_CINEMA_NAME];
		String address = value[COLUMN_INDEX_ADDRESS];
		String tel = value[COLUMN_INDEX_TEL];

		CinemaMstDTO dto = new CinemaMstDTO(cinemaId,regionId, cinemaName,address,tel);
		return dto;
	}
}
