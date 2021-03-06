package utility;

import java.io.FileNotFoundException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import com.esotericsoftware.yamlbeans.YamlException;

/**
 * Functions only relatives to matrix.org
 * @author jeangb
 */
public class MatrixUtilities {

	public static String getMatrixIdFromDisplayName(String displayName){
		return new StringBuilder("@").append(displayName).append(":").append(getHomeServerName()).toString();
	}

	/**
	 * Return homeserver name (ex: 'matrix.org'), depending of the homeserver choosed for the test, matrix.org or custome one.
	 * @return
	 */
	public static String getHomeServerName(){
		try {
			if("false".equals(ReadConfigFile.getInstance().getConfMap().get("homeserverlocal"))){
				return Constant.DEFAULT_MATRIX_SERVER;
			}else{
				return ReadConfigFile.getInstance().getConfMap().get("homeserver");
			}
		} catch (FileNotFoundException | YamlException e) {
			return null;
		}
	}
	/**
	 * Return custom home server URL.</br>
	 * If fromLocal = true, return URL with 'localhost'.
	 * @param fromLocal
	 * @return
	 */
	public static String getLocalHomeServerUrl(Boolean fromLocal, Boolean useConfigFile){
		String address = null,port = null;
		try {
			address = getLocalHomeServerAddress(useConfigFile);
			port=ReadConfigFile.getInstance().getConfMap().get("homeserver_port");
		} catch (FileNotFoundException | YamlException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(fromLocal){
			return new StringBuilder("https://localhost:").append(port).toString();
		}else{
			return new StringBuilder("https://").append(address).append(":").append(port).toString();
		}		
	}
	
	public static String getLocalHomeServerAddress(Boolean useConfigFile) throws FileNotFoundException, YamlException{
		if (useConfigFile){
			return ReadConfigFile.getInstance().getConfMap().get("homeserver_address");
		}else{
			try {
				return InetAddress.getLocalHost().getHostAddress();
			} catch (UnknownHostException e) {
				System.out.println("Impossible to get machine IP address");
				return null;
			}
		}
	}

	/**
	 * According to homeserverlocal value in config.yaml, return default home server or custom one.
	 * @return
	 */
	public static String getHomeServerUrlForRequestToMatrix(Boolean useConfigFileToGetHsAddress){
		try {
			if("false".equals(ReadConfigFile.getInstance().getConfMap().get("homeserverlocal"))){
				return Constant.DEFAULT_MATRIX_SERVER_URL;
			}else{
				return getLocalHomeServerUrl(true, useConfigFileToGetHsAddress);
			}
		} catch (FileNotFoundException | YamlException e) {
			e.printStackTrace();
			return null;
		}
	}
}
