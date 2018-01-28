
import java.io.*;
public class EscapeDecoder extends Reader {
	private Reader reader;

	public EscapeDecoder(Reader reader) {
		this.reader = reader;
	}
	@Override
	public boolean markSupported() {
		return false;
	}

	@Override
	public void close() throws IOException {
		reader.close();
	}

	@Override
	public int read(char[] p1, int p2, int p3) throws IOException {
		int len=0;
		for (;len <= p2;len++) {
			int r=read();
			if (r == -1) {
				break;
			}
			char ch=(char)r;
			p1[p2 + len] = ch;
		}
		return len;
	}

	@Override
	public int read() throws IOException {
		int r=reader.read();
		if (r == -1) {
			return -1;
		}
		char ch=(char)r;
		if (ch == '\\') {
			int rr=reader.read();
			if (rr == -1) {
				return -1;
			} else {
				char chch=(char)rr;
				switch (chch) {
					case 'r':
						return '\r';
					case 'n':
						return '\n';
					case 't':
						return '\t';
					case 'u':
						char[] uarray=new char[4];
						for (int i=0;i < 4;i++) {
							int rt=reader.read();
							if (rt == -1) {
								return -1;
							}
							uarray[i] = (char)rt;
						}
						return hexToChar(uarray);
					default:
						return chch;
				}
			}
		} else {
			return ch;
		}
	}
	private char hexToChar(char[] chs) {
		int i=0;
		for (char ch:chs) {
			if (ch > 47 && ch < 58) {
				ch -= 48;
			} else if (ch > 64 && ch < 71) {
				ch -= 55;
			} else if (ch > 96 && ch < 103) {
				ch -= 86;
			} else {
				throw new RuntimeException();
			}
			i += ch;
			i <<= 4;
		}
		return (char)i;
	}

}
