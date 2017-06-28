package sk.berops.android.vehiculum.dataModel;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Currency {
	
	private Unit unit;

	public enum Unit{
		// TODO: change the long currency names to local languages
		//EUR (0, "€", "EUR", "Euro"), << -- not to define it twice
		AED	(1, "د.إ", "AED", "Emirati Dirham"),
		AFN	(2, "؋", "AFN", "Afghan Afghani"),
		ALL	(3, "Lek", "ALL", "Albanian Lek"),
		AMD	(4, "AMD", "AMD", "Armenian Dram"),
		ANG	(5, "ƒ", "ANG", "Dutch Guilder"),
		AOA	(6, "Kz", "AOA", "Angolan Kwanza"),
		ARS	(7, "$", "ARS", "Argentine Peso"),
		AUD	(8, "$", "AUD", "Australian Dollar"),
		AWG	(9, "ƒ", "AWG", "Aruban Guilder"),
		AZN	(10, "ман", "AZN", "Azerbaijani New Manat"),
		BAM	(11, "KM", "BAM", "Bosnian Convertible Marka"),
		BBD	(12, "$", "BBD", "Barbadian Dollar"),
		BDT	(13, "Tk", "BDT", "Bangladeshi Taka"),
		BGN	(14, "лв", "BGN", "Bulgarian Lev"),
		BHD	(15, "BD", "BHD", "Bahraini Dinar"),
		BIF	(16, "FBu", "BIF", "Burundian Franc"),
		BMD	(17, "$", "BMD", "Bermudian Dollar"),
		BND	(18, "$", "BND", "Bruneian Dollar"),
		BOB	(19, "$b", "BOB", "Bolivian Bolíviano"),
		BRL	(20, "R$", "BRL", "Brazilian Real"),
		BSD	(21, "$", "BSD", "Bahamian Dollar"),
		BTN	(22, "Nu.", "BTN", "Bhutanese Ngultrum"),
		BWP	(23, "P", "BWP", "Botswana Pula"),
		BYN	(24, "Br", "BYN", "Belarusian Ruble"),
		BZD	(25, "BZ$", "BZD", "Belizean Dollar"),
		CAD	(26, "$", "CAD", "Canadian Dollar"),
		CDF	(27, "FC", "CDF", "Congolese Franc"),
		CHF	(28, "CHF", "CHF", "Swiss Franc"),
		CLP	(29, "$", "CLP", "Chilean Peso"),
		CNY	(30, "元", "CNY", "Chinese Yuan Renminbi"),
		COP	(31, "$", "COP", "Colombian Peso"),
		CRC	(32, "₡", "CRC", "Costa Rican Colon"),
		CUC	(33, "CUC$", "CUC", "Cuban Convertible Peso"),
		CUP	(34, "₱", "CUP", "Cuban Peso"),
		CVE	(35, "$", "CVE", "Cape Verdean Escudo"),
		CZK	(36, "Kč", "CZK", "Czech Koruna"),
		DJF	(37, "Fdj", "DJF", "Djiboutian Franc"),
		DKK	(38, "kr", "DKK", "Danish Krone"),
		DOP	(39, "RD$", "DOP", "Dominican Peso"),
		DZD	(40, "DA", "DZD", "Algerian Dinar"),
		EGP	(41, "£", "EGP", "Egyptian Pound"),
		ERN	(42, "Nfk", "ERN", "Eritrean Nakfa"),
		ETB	(43, "Br", "ETB", "Ethiopian Birr"),
		EUR	(44, "€", "EUR", "Euro"),
		FJD (45, "$", "FJD", "Fijian Dollar"),
		FKP	(46, "£", "FKP", "Falkland Islands Pound"),
		GBP	(47, "£", "GBP", "British Pound"),
		GEL	(48, "ლ", "GEL", "Georgian Lari"),
		GGP	(49, "£", "GGP", "Guernsey Pound"),
		GHS	(50, "GH¢", "GHS", "Ghana Cedi"),
		GIP	(51, "£", "GIP", "Gibraltar Pound"),
		GMD	(52, "D", "GMD", "Gambian Dalasi"),
		GNF	(53, "FG", "GNF", "Guinean Franc"),
		GTQ	(54, "Q", "GTQ", "Guatemalan Quetzal"),
		GYD	(55, "$", "GYD", "Guyanese Dollar"),
		HKD	(56, "HK$", "HKD", "Hong Kong Dollar"),
		HNL	(57, "L", "HNL", "Honduran Lempira"),
		HRK	(58, "kn", "HRK", "Croatian Kuna"),
		HTG	(59, "G", "HTG", "Haitian Gourde"),
		HUF	(60, "Ft", "HUF", "Hungarian Forint"),
		IDR	(61, "Rp", "IDR", "Indonesian Rupiah"),
		ILS	(62, "₪", "ILS", "Israeli Shekel"),
		IMP	(63, "£", "IMP", "Isle of Man Pound"),
		INR	(64, "₹", "INR", "Indian Rupee"),
		IQD	(65, "د.ع", "IQD", "Iraqi Dinar"),
		IRR	(66, "﷼", "IRR", "Iranian Rial"),
		ISK	(67, "kr", "ISK", "Icelandic Krona"),
		JEP	(68, "£", "JEP", "Jersey Pound"),
		JMD	(69, "J$", "JMD", "Jamaican Dollar"),
		JOD	(70, "د.ا", "JOD", "Jordanian Dinar"),
		JPY	(71, "¥", "JPN", "Japanese Yen"),
		KES	(72, "KSh", "KES", "Kenyan Shilling"),
		KGS	(73, "лв", "KGS", "Kyrgyzstani Som"),
		KHR	(74, "៛", "KHR", "Cambodian Riel"),
		KMF	(75, "CF", "KMF", "Comorian Franc"),
		KPW	(76, "₩", "KPW", "North Korean Won"),
		KRW	(77, "₩", "KRW", "South Korean Won"),
		KWD	(78, "ك", "KWD", "Kuwaiti Dinar"),
		KYD	(79, "$", "KYD", "Caymanian Dollar"),
		KZT	(80, "₸", "KZT", "Kazakhstani Tenge"),
		LAK	(81, "₭", "LAK", "Laotian Kip"),
		LBP	(82, "ل.ل", "LBP", "Lebanese Pound"),
		LKR	(83, "₨", "LKR", "Sri Lankan Rupee"),
		LRD	(84, "$", "LRD", "Liberian Dollar"),
		LSL	(85, "L", "LSL", "Lesotho Loti"),
		LYD	(86, "LD", "LYD", "Libyan Dinar"),
		MAD	(87, "MAD", "MAD", "Moroccon Dirham"),
		MDL	(88, "L", "MDL", "Moldovan Leu"),
		MGA	(89, "Ar", "MGA", "Malagasy Ariary"),
		MKD	(90, "ден", "MKD", "Macedonian Denar"),
		MMK	(91, "K", "MMK", "Burmese Kyat"),
		MNT	(92, "₮", "MNT", "Mongolian Tughrik"),
		MOP	(93, "MOP$", "MOP", "Macau Pataca"),
		MRO	(94, "UM", "MRO", "Mauritanian Ouguiya"),
		MUR	(95, "₨", "MUR", "Mauritian Rupee"),
		MVR	(96, "Rf", "MVR", "Maldivian Rufiyaa"),
		MWK	(97, "MK", "MWK", "Malawian Kwacha"),
		MXN	(98, "$", "MXN", "Mexican Peso"),
		MYR	(99, "RM", "MYR", "Malaysian Ringgit"),
		MZN	(100, "MT", "MZN", "Mozambican Metical"),
		NAD	(101, "$", "NAD", "Namibian Dollar"),
		NGN	(102, "₦", "NGN", "Nigerian Naira"),
		NIO	(103, "C$", "NIO", "Nicaraguan Cordoba"),
		NOK	(104, "kr", "NOK", "Norwegian Krone"),
		NPR	(105, "₨", "NPR", "Nepalese Rupee"),
		NZD	(106, "$", "NZD", "New Zealand Dollar"),
		OMR	(107, "﷼", "OMR", "Omani Rial"),
		PAB	(108, "B/.", "PAB", "Panamian Balboa"),
		PEN	(109, "S/.", "PEN", "Peruvian Sol"),
		PGK	(110, "K", "PGK", "Papua New Guinean Kina"),
		PHP	(111, "₱", "PHP", "Philippine Peso"),
		PKR	(112, "₨", "PKR", "Pakistani Rupee"),
		PLN	(113, "zł", "PLN", "Polish Zloty"),
		PYG	(114, "Gs", "PYG", "Paraguayan Guarani"),
		QAR	(115, "﷼", "QAR", "Qatari Riyal"),
		RON	(116, "lei", "RON", "Romanian Leu"),
		RSD	(117, "РСД", "RSD", "Serbian Dinar"),
		RUB	(118, "руб", "RUB", "Russian Ruble"),
		RWF	(119, "FRw", "RWF", "Rwandan Franc"),
		SAR	(120, "﷼", "SAR", "Saudi Arabian Riyal"),
		SBD	(121, "$", "SBD", "Solomon Islands Dollar"),
		SCR	(122, "₨", "SCR", "Seychelles Rupee"),
		SDG	(123, "ج.س.", "SDG", "Sudanese Pound"),
		SEK	(124, "kr", "SEK", "Swedish Krona"),
		SGD	(125, "$", "SGD", "Singapore Dollar"),
		SHP	(126, "£", "SHP", "Saint Helenian Pound"),
		SLL	(127, "Le", "SLL", "Sierra Leonean Leone"),
		SOS	(128, "S", "SOS", "Somali Shilling"),
		SRD	(129, "$", "SRD", "Surinamese Dollar"),
		STD	(130, "Db", "STD", "São Tomé and Príncipe Dobra"),
		SVC	(131, "$", "SVC", "El Salvadoran Colon"),
		SYP	(132, "£", "SYP", "Syrian Pound"),
		SZL	(133, "L", "SZL", "Swazi Lilangeni"),
		THB	(134, "฿", "THB", "Thai Baht"),
		TJS	(135, "SM", "TJS", "Tajikistani Somoni"),
		TMT	(136, "T", "TMT", "Turkmenistan Manat"),
		TND	(137, "DT", "TND", "Tunisian Dinar"),
		TOP	(138, "T$", "TOP", "Tongan Pa'anga"),
		TRY	(139, "₺", "TRY", "Turkish Lira"),
		TTD	(140, "TT$", "TTD", "Trinidadian Dollar"),
		TVD	(141, "$", "TVD", "Tuvaluan Dollar"),
		TWD	(142, "NT$", "TWD", "Taiwan New Dollar"),
		TZS	(143, "TSh", "TZS", "Tanzanian Shilling"),
		UAH	(144, "₴", "UAH", "Ukrainian Hryvnia"),
		UGX	(145, "UGX", "UGX", "Ugandan Shilling"),
		USD	(146, "$", "USD", "US Dollar"),
		UYU	(147, "$U", "UYU", "Uruguayan Peso"),
		UZS	(148, "лв", "UZS", "Uzbekistani Som"),
		VEF	(149, "Bs.", "VEF", "Venezuelan Bolivar"),
		VND	(150, "₫", "VND", "Vietnamese Dong"),
		VUV	(151, "VT", "VUV", "Ni-Vanuatu Vatu"),
		WST	(152, "$", "WST", "Samoan Tala"),
		XAF	(153, "FCFA", "XAF", "Central African CFA Franc"),
		XCD	(154, "$", "XCD", "East Caribbean Dollar"),
		XOF	(155, "CFA", "XOF", "West African CFA Franc"),
		XPF	(156, "₣", "XPF", "CFP Franc"),
		YER	(157, "﷼", "YER", "Yemeni Rial"),
		ZAR	(158, "R", "ZAR", "South African Rand"),
		ZMW	(169, "ZK", "ZMW", "Zambian Kwacha"),
		ZWD	(160, "Z$", "ZWD", "Zimbabwean Dollar");

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
			
			Unit result = null;
			result = idToUnitMapping.get(id);
			return result;
		}
		
		private static void initMapping() {
			idToUnitMapping = new HashMap<>();
			for (Unit unit : values()) {
				idToUnitMapping.put(unit.id, unit);
			}
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
