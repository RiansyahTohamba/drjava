class Isolated{
	// CINT = 14 matches
	private File _locateClassFile() {
		if (isUntitled()) return FileOps.NULL_FILE;
		String className;
		try { className = getDocument().getQualifiedClassName(); }
		catch (ClassNameNotFoundException cnnfe) {
			_log.log("_locateClassFile() failed for " + this + " because getQualifedClassName returned ClassNotFound");
			return FileOps.NULL_FILE;  /* No source class name */ 
		}

		String ps = System.getProperty("file.separator");
		// replace periods with the System's file separator
		className = StringOps.replace(className, ".", ps);
		String fileName = className + ".class";

		// Check source root set (open files)
		ArrayList<File> roots = new ArrayList<File>();

		if (getBuildDirectory() != FileOps.NULL_FILE) roots.add(getBuildDirectory());

		// Add the current document to the beginning of the roots list
		try {
			File root = getSourceRoot();
			roots.add(root); 
		}
		catch (InvalidPackageException ipe) {
			try {
			//todo: extract root variable, duplicate getFile().getParentFile
			  File root = getFile().getParentFile();
			  if (root != FileOps.NULL_FILE) {
			    roots.add(root);
			  }
			}
			catch(NullPointerException e) { throw new UnexpectedException(e); }
			catch(FileMovedException fme) {
			  // Moved, but we'll add the old file to the set anyway
			  _log.log("File for " + this + "has moved; adding parent directory to list of roots");
			  
			  // todo: extract
			  File root = fme.getFile().getParentFile();
			  if (root != FileOps.NULL_FILE) roots.add(root);
			}
		}

		File classFile = findFileInPaths(fileName, roots);
		if (classFile != FileOps.NULL_FILE) {
		return classFile;
		}
		// Class not on source root set, check system classpath
		classFile = findFileInPaths(fileName, ReflectUtil.SYSTEM_CLASS_PATH);
		if (classFile != FileOps.NULL_FILE) return classFile;
		// not on system classpath, check interactions classpath

		// todo: extract
		Vector<File> cpSetting = DrJava.getConfig().getSetting(EXTRA_CLASSPATH);
		return findFileInPaths(fileName, cpSetting);
    }

}