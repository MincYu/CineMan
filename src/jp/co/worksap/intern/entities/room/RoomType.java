package jp.co.worksap.intern.entities.room;

public enum RoomType {
	TWO_D, THREE_D, VIP, IMAX;

	public String toString() {
		switch (this) {
		case TWO_D:
			return "2D";
		case THREE_D:
			return "3D";
		case VIP:
			return "VIP";
		case IMAX:
			return "IMAX";
		default:
			return ""; 
		}
	}

	public static RoomType valueOfString(String src) {
		String raw = src.toUpperCase();
		if (raw.equals("IMAX")) {
			return IMAX;
		}

		if (raw.equals("VIP")) {
			return VIP;
		}
		
		if (raw.equals("3D")) {
			return THREE_D;
		}

		if (raw.equals("2D")) {
			return TWO_D;
		}

		throw new IllegalArgumentException("Unknown Room Type : " + raw);
	}
}
