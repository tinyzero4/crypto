package com.breedish.crypto.set1;

import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.lang.System.out;

public class C1
{
	public static String hex2base64(String in)
	{
		return Base64.getEncoder().encodeToString(hexToByte(in));
	}

	private static byte[] hexToByte(String in)
	{
		var raw = new byte[in.length() / 2];

		for (int i = 0; i < in.length(); i += 2)
		{
			raw[i / 2] = (byte) (Character.digit(in.charAt(i), 16) << 4 | Character.digit(in.charAt(i + 1), 16));
		}
		return raw;
	}

	public static String fixedXOR(String in1, String in2)
	{
		var raw1 = hexToByte(in1);
		var raw2 = hexToByte(in2);
		var raw3 = new byte[raw1.length];
		for (int i = 0; i < raw1.length; i++)
		{
			raw3[i] = (byte) (raw1[i] ^ raw2[i]);
		}
		return byteToHex(raw3);
	}

	private static String byteToHex(byte[] in)
	{
		var buffer = new StringBuilder();

		for (byte b : in)
		{
			buffer.append(Character.forDigit((b >> 4) & 0xF, 16));
			buffer.append(Character.forDigit(b & 0xF, 16));
		}

		return buffer.toString();
	}


	public static List<Map.Entry<Character, Long>> lettersFrequency(String in)
	{
		var freq = in.chars().mapToObj(c -> (char) c)
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

		return freq.entrySet().stream().sorted(Comparator.comparing(Map.Entry::getValue)).collect(Collectors.toList());
	}

	public static byte[] charXOR(byte[] in, Character c) {
		var res = new byte[in.length];
		for (int i = 0; i < in.length; i++)
		{
			res[i] = (byte) (in[i] ^ c);
		}
		return res;
	}

	public static void XORCipher(String in)
	{
		byte[] bytes = hexToByte(in);
		var freq = lettersFrequency(new String(bytes));

		freq.forEach(e -> {
			var candidate = charXOR(bytes, e.getKey());
			out.println(e.getKey() + " : " + e.getValue() + " : " + new String(candidate));
		});
	}

	public static void main(String[] args)
	{
		out.println(hex2base64("49276d206b696c6c696e6720796f757220627261696e206c696b65206120706f69736f6e6f7573206d757368726f6f6d"));
		out.println(fixedXOR("1c0111001f010100061a024b53535009181c", "686974207468652062756c6c277320657965"));
		XORCipher("1b37373331363f78151b7f2b783431333d78397828372d363c78373e783a393b3736");



	}
}
