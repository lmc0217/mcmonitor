package com.mcmonitor.tool;

import java.io.File;

import com.mcmonitor.log.Log;

/**
 *  运行环境处理log日记
 * @author liu mengchao
 * @createDate 2017.1.22
 */
public class MyEnvironment {
	public static boolean mkEviroment() {
		if (!mkMyDir()) {
			return false;
		}

		return true;
	}

	private static boolean mkMyDir() {
		String[] paths = { Log.PATH_LOG, Log.PATH_CONF};
		for (String path : paths) {
			if (!mkDirFromWorkdir(path)) {
				Log.err.info("mk dir error:{}",path);
				return false;
			}
		}
		return true;
	}
	
	private static boolean mkDirFromWorkdir(String dir) {
		if (dir == null || dir.isEmpty()) {
			return false;
		}
		File file = new File(dir);
		if (file.isDirectory()) {
			return true;
		}
		return file.mkdir();
	}
}
