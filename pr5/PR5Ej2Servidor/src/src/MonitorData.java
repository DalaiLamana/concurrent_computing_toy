package src;

import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class MonitorData{
	private HashMap<String, Flujos> _flujos;
	private String _server_ip;
	private HashMap<String, ArrayList<String>> _info;
	//private int usuarios_registrados; //Para llevar la cuenta de los que ya hay
	public MonitorData(){
		this._flujos = new HashMap<String, Flujos>();
		this._server_ip = "localhost";
		this._info = new HashMap<String, ArrayList<String>>();
	}
	public MonitorData(String ip) {
		this._flujos = new HashMap<String, Flujos>();
		this._server_ip = ip;
		this._info = new HashMap<String, ArrayList<String>>();
	}
	public void putInfo(String id, ArrayList<String> info) {
		this._info.put(id, info);
	}
	public synchronized void putFlujos(String id, Flujos f) {
		this._flujos.put(id, f);
		
	}
	public synchronized void remove_info(String id) {
		this._info.remove(id);
	}
	public synchronized void remove_flujos(String id) {
		this._flujos.remove(id);
	}
	//Getters & setters
	public synchronized HashMap<String, Flujos> get_flujos() {
		return _flujos;
	}
	public synchronized void set_flujos(HashMap<String, Flujos> _flujos) {
		this._flujos = _flujos;
	}
	public synchronized HashMap<String, ArrayList<String>> get_info() {
		return _info;
	}
	public synchronized void set_info(HashMap<String, ArrayList<String>> _info) {
		this._info = _info;
	}
	public String get_server_ip() {
		return _server_ip;
	}
	public void set_server_ip(String _server_ip) {
		this._server_ip = _server_ip;
	}
	public synchronized String get_client_by_file(String file) {
		for(HashMap.Entry<String, ArrayList<String>> entry : _info.entrySet()) {
			if(entry.getValue().contains(file)) {
				return entry.getKey();
			}
		}
		return null;
	}
	public ObjectOutputStream get_fout_by_client(String id) {
		return this._flujos.get(id).get_fout();
		
	}
	public void addInfo(String id, String data) {
		this._info.get(id).add(data);
	}
}
