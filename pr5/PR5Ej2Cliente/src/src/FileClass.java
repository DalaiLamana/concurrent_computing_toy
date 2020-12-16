package src;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.Scanner;

public class FileClass implements Serializable{
	private static final long serialVersionUID = -287339527504807100L;
	private String _name;
	private String _content;
	public FileClass(String path) throws IOException, FileNotFoundException {
		this._name = path;
		BufferedReader br = new BufferedReader(new FileReader(path));
		Scanner sc = new Scanner(new File(path));
		_content = sc.useDelimiter("\\Z").next();
		sc.close();
		br.close();
	}
	public void save_file() throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(_name));
		bw.write(_content);
		bw.close();
	}
	public String get_name() {
		return _name;
	}
	public void set_name(String _name) {
		this._name = _name;
	}
	public String get_content() {
		return _content;
	}
	public void set_content(String _content) {
		this._content = _content;
	}
	
}
