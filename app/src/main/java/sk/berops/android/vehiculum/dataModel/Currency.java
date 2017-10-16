package sk.berops.android.vehiculum.dataModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import sk.berops.android.vehiculum.gui.common.DualString;

public class Currency {
	
	private Unit unit;

	public enum Unit{
		// TODO: change the long currency names to local languages
		AED	(0, "د.إ", "AED", "Emirati Dirham"),
		AFN	(1, "؋", "AFN", "Afghan Afghani"),
		ALL	(2, "Lek", "ALL", "Albanian Lek"),
		AMD	(3, "AMD", "AMD", "Armenian Dram"),
		ANG	(4, "ƒ", "ANG", "Dutch Guilder"),
		AOA	(5, "Kz", "AOA", "Angolan Kwanza"),
		ARS	(6, "$", "ARS", "Argentine Peso"),
		AUD	(7, "$", "AUD", "Australian Dollar"),
		AWG	(8, "ƒ", "AWG", "Aruban Guilder"),
		AZN	(9, "ман", "AZN", "Azerbaijani New Manat"),
		BAM	(10, "KM", "BAM", "Bosnian Convertible Marka"),
		BBD	(11, "$", "BBD", "Barbadian Dollar"),
		BDT	(12, "Tk", "BDT", "Bangladeshi Taka"),
		BGN	(13, "лв", "BGN", "Bulgarian Lev"),
		BHD	(14, "BD", "BHD", "Bahraini Dinar"),
		BIF	(15, "FBu", "BIF", "Burundian Franc"),
		BMD	(16, "$", "BMD", "Bermudian Dollar"),
		BND	(17, "$", "BND", "Bruneian Dollar"),
		BOB	(18, "$b", "BOB", "Bolivian Bolíviano"),
		BRL	(19, "R$", "BRL", "Brazilian Real"),
		BSD	(20, "$", "BSD", "Bahamian Dollar"),
		BTN	(21, "Nu.", "BTN", "Bhutanese Ngultrum"),
		BWP	(22, "P", "BWP", "Botswana Pula"),
		BYN	(23, "Br", "BYN", "Belarusian Ruble"),
		BZD	(24, "BZ$", "BZD", "Belizean Dollar"),
		CAD	(25, "$", "CAD", "Canadian Dollar"),
		CDF	(26, "FC", "CDF", "Congolese Franc"),
		CHF	(27, "CHF", "CHF", "Swiss Franc"),
		CLP	(28, "$", "CLP", "Chilean Peso"),
		CNY	(29, "元", "CNY", "Chinese Yuan Renminbi"),
		COP	(30, "$", "COP", "Colombian Peso"),
		CRC	(31, "₡", "CRC", "Costa Rican Colon"),
		CUC	(32, "CUC$", "CUC", "Cuban Convertible Peso"),
		CUP	(33, "₱", "CUP", "Cuban Peso"),
		CVE	(34, "$", "CVE", "Cape Verdean Escudo"),
		CZK	(35, "Kč", "CZK", "Czech Koruna"),
		DJF	(36, "Fdj", "DJF", "Djiboutian Franc"),
		DKK	(37, "kr", "DKK", "Danish Krone"),
		DOP	(38, "RD$", "DOP", "Dominican Peso"),
		DZD	(39, "DA", "DZD", "Algerian Dinar"),
		EGP	(40, "£", "EGP", "Egyptian Pound"),
		ERN	(41, "Nfk", "ERN", "Eritrean Nakfa"),
		ETB	(42, "Br", "ETB", "Ethiopian Birr"),
		EUR	(43, "€", "EUR", "Euro"),
		FJD (44, "$", "FJD", "Fijian Dollar"),
		FKP	(45, "£", "FKP", "Falkland Islands Pound"),
		GBP	(46, "£", "GBP", "British Pound"),
		GEL	(47, "ლ", "GEL", "Georgian Lari"),
		GGP	(48, "£", "GGP", "Guernsey Pound"),
		GHS	(49, "GH¢", "GHS", "Ghana Cedi"),
		GIP	(50, "£", "GIP", "Gibraltar Pound"),
		GMD	(51, "D", "GMD", "Gambian Dalasi"),
		GNF	(52, "FG", "GNF", "Guinean Franc"),
		GTQ	(53, "Q", "GTQ", "Guatemalan Quetzal"),
		GYD	(54, "$", "GYD", "Guyanese Dollar"),
		HKD	(55, "HK$", "HKD", "Hong Kong Dollar"),
		HNL	(56, "L", "HNL", "Honduran Lempira"),
		HRK	(57, "kn", "HRK", "Croatian Kuna"),
		HTG	(58, "G", "HTG", "Haitian Gourde"),
		HUF	(59, "Ft", "HUF", "Hungarian Forint"),
		IDR	(60, "Rp", "IDR", "Indonesian Rupiah"),
		ILS	(61, "₪", "ILS", "Israeli Shekel"),
		IMP	(62, "£", "IMP", "Isle of Man Pound"),
		INR	(63, "₹", "INR", "Indian Rupee"),
		IQD	(64, "د.ع", "IQD", "Iraqi Dinar"),
		IRR	(65, "﷼", "IRR", "Iranian Rial"),
		ISK	(66, "kr", "ISK", "Icelandic Krona"),
		JEP	(67, "£", "JEP", "Jersey Pound"),
		JMD	(68, "J$", "JMD", "Jamaican Dollar"),
		JOD	(69, "د.ا", "JOD", "Jordanian Dinar"),
		JPY	(70, "¥", "JPN", "Japanese Yen"),
		KES	(71, "KSh", "KES", "Kenyan Shilling"),
		KGS	(72, "лв", "KGS", "Kyrgyzstani Som"),
		KHR	(73, "៛", "KHR", "Cambodian Riel"),
		KMF	(74, "CF", "KMF", "Comorian Franc"),
		KPW	(75, "₩", "KPW", "North Korean Won"),
		KRW	(76, "₩", "KRW", "South Korean Won"),
		KWD	(77, "ك", "KWD", "Kuwaiti Dinar"),
		KYD	(78, "$", "KYD", "Caymanian Dollar"),
		KZT	(79, "₸", "KZT", "Kazakhstani Tenge"),
		LAK	(80, "₭", "LAK", "Laotian Kip"),
		LBP	(81, "ل.ل", "LBP", "Lebanese Pound"),
		LKR	(82, "₨", "LKR", "Sri Lankan Rupee"),
		LRD	(83, "$", "LRD", "Liberian Dollar"),
		LSL	(84, "L", "LSL", "Lesotho Loti"),
		LYD	(85, "LD", "LYD", "Libyan Dinar"),
		MAD	(86, "MAD", "MAD", "Moroccon Dirham"),
		MDL	(87, "L", "MDL", "Moldovan Leu"),
		MGA	(88, "Ar", "MGA", "Malagasy Ariary"),
		MKD	(89, "ден", "MKD", "Macedonian Denar"),
		MMK	(90, "K", "MMK", "Burmese Kyat"),
		MNT	(91, "₮", "MNT", "Mongolian Tughrik"),
		MOP	(92, "MOP$", "MOP", "Macau Pataca"),
		MRO	(93, "UM", "MRO", "Mauritanian Ouguiya"),
		MUR	(94, "₨", "MUR", "Mauritian Rupee"),
		MVR	(95, "Rf", "MVR", "Maldivian Rufiyaa"),
		MWK	(96, "MK", "MWK", "Malawian Kwacha"),
		MXN	(97, "$", "MXN", "Mexican Peso"),
		MYR	(98, "RM", "MYR", "Malaysian Ringgit"),
		MZN	(99, "MT", "MZN", "Mozambican Metical"),
		NAD	(100, "$", "NAD", "Namibian Dollar"),
		NGN	(101, "₦", "NGN", "Nigerian Naira"),
		NIO	(102, "C$", "NIO", "Nicaraguan Cordoba"),
		NOK	(103, "kr", "NOK", "Norwegian Krone"),
		NPR	(104, "₨", "NPR", "Nepalese Rupee"),
		NZD	(105, "$", "NZD", "New Zealand Dollar"),
		OMR	(106, "﷼", "OMR", "Omani Rial"),
		PAB	(107, "B/.", "PAB", "Panamian Balboa"),
		PEN	(108, "S/.", "PEN", "Peruvian Sol"),
		PGK	(109, "K", "PGK", "Papua New Guinean Kina"),
		PHP	(110, "₱", "PHP", "Philippine Peso"),
		PKR	(111, "₨", "PKR", "Pakistani Rupee"),
		PLN	(112, "zł", "PLN", "Polish Zloty"),
		PYG	(113, "Gs", "PYG", "Paraguayan Guarani"),
		QAR	(114, "﷼", "QAR", "Qatari Riyal"),
		RON	(115, "lei", "RON", "Romanian Leu"),
		RSD	(116, "РСД", "RSD", "Serbian Dinar"),
		RUB	(117, "руб", "RUB", "Russian Ruble"),
		RWF	(118, "FRw", "RWF", "Rwandan Franc"),
		SAR	(119, "﷼", "SAR", "Saudi Arabian Riyal"),
		SBD	(120, "$", "SBD", "Solomon Islands Dollar"),
		SCR	(121, "₨", "SCR", "Seychelles Rupee"),
		SDG	(122, "ج.س.", "SDG", "Sudanese Pound"),
		SEK	(123, "kr", "SEK", "Swedish Krona"),
		SGD	(124, "$", "SGD", "Singapore Dollar"),
		SHP	(125, "£", "SHP", "Saint Helenian Pound"),
		SLL	(126, "Le", "SLL", "Sierra Leonean Leone"),
		SOS	(127, "S", "SOS", "Somali Shilling"),
		SRD	(128, "$", "SRD", "Surinamese Dollar"),
		STD	(129, "Db", "STD", "São Tomé and Príncipe Dobra"),
		SVC	(130, "$", "SVC", "El Salvadoran Colon"),
		SYP	(131, "£", "SYP", "Syrian Pound"),
		SZL	(132, "L", "SZL", "Swazi Lilangeni"),
		THB	(133, "฿", "THB", "Thai Baht"),
		TJS	(134, "SM", "TJS", "Tajikistani Somoni"),
		TMT	(135, "T", "TMT", "Turkmenistan Manat"),
		TND	(136, "DT", "TND", "Tunisian Dinar"),
		TOP	(137, "T$", "TOP", "Tongan Pa'anga"),
		TRY	(138, "₺", "TRY", "Turkish Lira"),
		TTD	(139, "TT$", "TTD", "Trinidadian Dollar"),
		TVD	(140, "$", "TVD", "Tuvaluan Dollar"),
		TWD	(141, "NT$", "TWD", "Taiwan New Dollar"),
		TZS	(142, "TSh", "TZS", "Tanzanian Shilling"),
		UAH	(143, "₴", "UAH", "Ukrainian Hryvnia"),
		UGX	(144, "UGX", "UGX", "Ugandan Shilling"),
		USD	(145, "$", "USD", "US Dollar"),
		UYU	(146, "$U", "UYU", "Uruguayan Peso"),
		UZS	(147, "лв", "UZS", "Uzbekistani Som"),
		VEF	(148, "Bs.", "VEF", "Venezuelan Bolivar"),
		VND	(149, "₫", "VND", "Vietnamese Dong"),
		VUV	(150, "VT", "VUV", "Ni-Vanuatu Vatu"),
		WST	(151, "$", "WST", "Samoan Tala"),
		XAF	(152, "FCFA", "XAF", "Central African CFA Franc"),
		XCD	(153, "$", "XCD", "East Caribbean Dollar"),
		XOF	(154, "CFA", "XOF", "West African CFA Franc"),
		XPF	(155, "₣", "XPF", "CFP Franc"),
		YER	(156, "﷼", "YER", "Yemeni Rial"),
		ZAR	(157, "R", "ZAR", "South African Rand"),
		ZMW	(158, "ZK", "ZMW", "Zambian Kwacha"),
		ZWD	(159, "Z$", "ZWD", "Zimbabwean Dollar");

		private int id;
		private String symbol;
		/**
		 * ISO-4217 Currency code
		 */
		private String unitIsoCode;
		private String longUnit;
		Unit(int id, String symbol, String unitIsoCode, String longUnit) {
			this.setId(id);
			this.setSymbol(symbol);
			this.setUnitIsoCode(unitIsoCode);
			this.setLongUnit(longUnit);
		}
		
		private static Map<Integer, Unit> idToUnitMapping;

		public static Unit getUnit(int id) {
			if (idToUnitMapping == null) {
				initMapping();
			}

			if (id == 0) return EUR; // EUR is our default currency
			
			Unit result = idToUnitMapping.get(id);
			return result;
		}
		
		private static void initMapping() {
			idToUnitMapping = new HashMap<>();
			for (Unit unit : values()) {
				idToUnitMapping.put(unit.id, unit);
			}
		}

		public static ArrayList<CharSequence> extractSymbols() {
			ArrayList<CharSequence> result = new ArrayList<>();
			for (Unit u: Unit.values()) {
				result.add(u.getSymbol());
			}

			return result;
		}

		public static ArrayList<DualString> extractCodesAndSymbols() {
			ArrayList<DualString> result = new ArrayList<>();
			for (Unit u: Unit.values()) {
				CharSequence collapsed = u.getSymbol();
				CharSequence expanded = u.getUnitIsoCode() +" ("+ u.getSymbol() +")";
				result.add(new DualString(collapsed, expanded));
			}

			return result;
		}

		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getSymbol() {
			return symbol;
		}
		public void setSymbol(String symbol) {
			this.symbol = symbol;
		}
		public String getUnitIsoCode() {
			return unitIsoCode;
		}
		public void setUnitIsoCode(String unitIsoCode) {
			this.unitIsoCode = unitIsoCode;
		}
		public String getLongUnit() {
			return longUnit;
		}
		public void setLongUnit(String longUnit) {
			this.longUnit = longUnit;
		}

		@Override
		public String toString() {
			return getUnitIsoCode();
		}
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public static double convertToSI(double cost, Unit currency) {
		return convertToSI(cost, currency, new Date());
	}
	
	public static double convertToSI(double cost, Unit currency, Date eventDate) {
		return convert(cost, currency, Unit.EUR, eventDate);
	}
	
	public static double convert(double cost, Unit fromCurrency, Unit toCurrency) {
		return convert(cost, fromCurrency, toCurrency, new Date());
	}
	
	public static double convert(double cost, Unit fromCurrency, Unit toCurrency, Date eventDate) {
		return cost;
	}
}
