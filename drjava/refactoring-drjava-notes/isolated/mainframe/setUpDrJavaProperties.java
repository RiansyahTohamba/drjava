class Mainframe{
    /* 10x extract method
        getProjectFiles
        getAllFiles
        getProjectMode
        getProjectChanged
        getMasterWorkingDir
        getWorkingDir
        getCurrentCol
        getCurrentLine
        getDrJavaActionProperty
        getCurrentProperty
    */
    /** This method sets up all the DrJava properties that can be used as variables
     * in external process command lines. */
    public void setUpDrJavaProperties() {
        final String DEF_DIR = "${drjava.working.dir}";
        DrJavaPropertySetup.setup();
        // Files
        PropertyMaps.TEMPLATE.setProperty("DrJava", getCurrentProperty());
        PropertyMaps.TEMPLATE.setProperty("DrJava", getCurrentLine());
        PropertyMaps.TEMPLATE.setProperty("DrJava", getCurrentCol());
        PropertyMaps.TEMPLATE.setProperty("DrJava", getWorkingDir());
        PropertyMaps.TEMPLATE.setProperty("DrJava", getMasterWorkingDir());

        // Files
        PropertyMaps.TEMPLATE.setProperty("DrJava", getAllFiles(DEF_DIR));
        PropertyMaps.TEMPLATE.setProperty("DrJava",getProjectFiles(DEF_DIR)).
                listenToInvalidatesOf(PropertyMaps.TEMPLATE.getProperty("DrJava", "drjava.all.files"));

        PropertyMaps.TEMPLATE.setProperty("DrJava",
                new FileListProperty("drjava.included.files", File.pathSeparator, DEF_DIR,
                        "Returns a list of all files open in DrJava that are " +
                                "not underneath the project root but are included in " +
                                "the project.\n" +
                                "Optional attributes:\n" +
                                "\trel=\"<dir to which output should be relative\"\n" +
                                "\tsep=\"<separator between files>\"\n"+
                                "\tsquote=\"<true to enclose file in single quotes>\"\n"+
                                "\tdquote=\"<true to enclose file in double quotes>\"") {
                    protected List<File> getList(PropertyMaps pm) {
                        ArrayList<File> l = new ArrayList<File>();
                        for(OpenDefinitionsDocument odd: _model.getAuxiliaryDocuments()) {
                            l.add(odd.getRawFile());
                        }
                        return l;
                    }
                    public String getLazy(PropertyMaps pm) { return getCurrent(pm); }
                    public boolean isCurrent() { return false; }
                }).listenToInvalidatesOf(PropertyMaps.TEMPLATE.getProperty("DrJava", "drjava.all.files"));

        DrJavaProperty autoRefresh = new DrJavaProperty("project.auto.refresh",
                "Evaluates to true if project auto-refresh is enabled.") {
            public void update(PropertyMaps pm) {
                Boolean b = _model.getAutoRefreshStatus();
                String f = _attributes.get("fmt").toLowerCase();
                if (f.equals("int")) _value = b ? "1" : "0";
                else if (f.equals("yes")) _value = b ? "yes" : "no";
                else _value = b.toString();
            }
            public String getLazy(PropertyMaps pm) { return getCurrent(pm); }
            public void resetAttributes() {
                _attributes.clear();
                _attributes.put("fmt", "boolean");
            }
            public boolean isCurrent() { return false; }
        };

        FileListProperty excludedFiles = new FileListProperty("project.excluded.files", File.pathSeparator, DEF_DIR,
                "Returns a list of files that are excluded from DrJava's "+
                        "project auto-refresh.\n"+
                        "Optional attributes:\n"+
                        "\trel=\"<dir to which output should be relative\"\n"+
                        "\tsep=\"<separator between files>\"\n"+
                        "\tsquote=\"<true to enclose file in single quotes>\"\n"+
                        "\tdquote=\"<true to enclose file in double quotes>\"") {
            protected List<File> getList(PropertyMaps pm) {
                ArrayList<File> l = new ArrayList<File>();
                for(File f: _model.getExclFiles()) { l.add(f); }
                return l;
            }
            public String getLazy(PropertyMaps pm) { return getCurrent(pm); }
            public boolean isCurrent() { return false; }
        };

        PropertyMaps.TEMPLATE.setProperty("DrJava",
                new FileListProperty("drjava.external.files", File.pathSeparator, DEF_DIR,
                        "Returns a list of all files open in DrJava that are "+
                                "not underneath the project root and are not included in "+
                                "the project.\n"+
                                "Optional attributes:\n"+
                                "\trel=\"<dir to which output should be relative\"\n"+
                                "\tsep=\"<separator between files>\"\n"+
                                "\tsquote=\"<true to enclose file in single quotes>\"\n"+
                                "\tdquote=\"<true to enclose file in double quotes>\"") {
                    protected List<File> getList(PropertyMaps pm) {
                        ArrayList<File> l = new ArrayList<File>();
                        for(OpenDefinitionsDocument odd: _model.getNonProjectDocuments()) {
                            l.add(odd.getRawFile());
                        }
                        return l;
                    }
                    public String getLazy(PropertyMaps pm) { return getCurrent(pm); }
                    public boolean isCurrent() { return false; }
                }).listenToInvalidatesOf(PropertyMaps.TEMPLATE.getProperty("DrJava", "drjava.all.files"));

        PropertyMaps.TEMPLATE.
                setProperty("Misc",
                        new DrJavaProperty("input", "(User Input...)",
                                "Get an input string from the user.\n"+
                                        "Optional attributes:\n"+
                                        "\tprompt=\"<prompt to display>\"\n"+
                                        "\tdefault=\"<suggestion to the user>\"") {
                            public String toString() {
                                return "(User Input...)";
                            }
                            public void update(PropertyMaps pm) {
                                String msg = _attributes.get("prompt");
                                if (msg == null) msg = "Please enter text for the external process.";
                                String input = _attributes.get("default");
                                if (input == null) input = "";
                                input = JOptionPane.showInputDialog(MainFrame.this, msg, input);
                                if (input == null) input = _attributes.get("default");
                                if (input == null) input = "";
                                _value = input;
                            }
                            public String getCurrent(PropertyMaps pm) {
                                invalidate();
                                return super.getCurrent(pm);
                            }
                            public void resetAttributes() {
                                _attributes.clear();
                                _attributes.put("prompt", null);
                                _attributes.put("default", null);
                            }
                            public boolean isCurrent() { return false; }
                        });

        // Project
        PropertyMaps.TEMPLATE.setProperty("Project", getProjectMode());
        PropertyMaps.TEMPLATE.setProperty("Project", getProjectChanged());

        PropertyMaps.TEMPLATE.setProperty("Project",
                new FileProperty("project.file",
                        new Thunk<File>() {
                            public File value() { return _model.getProjectFile(); }
                        },
                        "Returns the current project file in DrJava.\n"+
                                "Optional attributes:\n"+
                                "\trel=\"<dir to which the output should be relative\"\n"+
                                "\tsquote=\"<true to enclose file in single quotes>\"\n"+
                                "\tdquote=\"<true to enclose file in double quotes>\"") {
                    public String getLazy(PropertyMaps pm) { return getCurrent(pm); }
                });

        PropertyMaps.TEMPLATE.
                setProperty("Project",
                        new FileProperty("project.main.class",
                                new Thunk<File>() {
                                    public File value() { return new File(_model.getMainClass()); }
                                },
                                "Returns the current project file in DrJava.\n"+
                                        "Optional attributes:\n"+
                                        "\trel=\"<dir to which the output should be relative\"\n"+
                                        "\tsquote=\"<true to enclose file in single quotes>\"\n"+
                                        "\tdquote=\"<true to enclose file in double quotes>\"") {
                            public String getLazy(PropertyMaps pm) { return getCurrent(pm); }
                        });
        PropertyMaps.TEMPLATE.
                setProperty("Project",
                        new FileProperty("project.root",
                                new Thunk<File>() {
                                    public File value() { return _model.getProjectRoot(); }
                                },
                                "Returns the current project root in DrJava.\n"+
                                        "Optional attributes:\n"+
                                        "\trel=\"<dir to which the output should be relative\"\n"+
                                        "\tsquote=\"<true to enclose file in single quotes>\"\n"+
                                        "\tdquote=\"<true to enclose file in double quotes>\"") {
                            public String getLazy(PropertyMaps pm) { return getCurrent(pm); }
                        });
        PropertyMaps.TEMPLATE.setProperty("Project",new FileProperty("project.build.dir",new Thunk<File>() {
            public File value() { return _model.getBuildDirectory(); }
        },
                "Returns the current build directory in DrJava.\n"+
                        "Optional attributes:\n"+
                        "\trel=\"<dir to which the output should be relative\"\n"+
                        "\tsquote=\"<true to enclose file in single quotes>\"\n"+
                        "\tdquote=\"<true to enclose file in double quotes>\"") {
            public String getLazy(PropertyMaps pm) { return getCurrent(pm); }
        });
        RecursiveFileListProperty classFilesProperty =
                new RecursiveFileListProperty("project.class.files", File.pathSeparator, DEF_DIR,
                        _model.getBuildDirectory().getAbsolutePath(),
                        "Returns the class files currently in the build directory.\n"+
                                "\trel=\"<dir to which the output should be relative\"\n"+
                                "\tsep=\"<string to separate files in the list>\"\n"+
                                "\tsquote=\"<true to enclose file in single quotes>\"\n"+
                                "\tdquote=\"<true to enclose file in double quotes>\"") {
                    /** Reset the attributes. */
                    public void resetAttributes() {
                        _attributes.clear();
                        _attributes.put("sep", _sep);
                        _attributes.put("rel", _dir);
                        _attributes.put("dir", _model.getBuildDirectory().getAbsolutePath());
                        _attributes.put("filter", "*.class");
                        _attributes.put("dirfilter", "*");
                    }
                };
        PropertyMaps.TEMPLATE.setProperty("Project", classFilesProperty);



        PropertyMaps.TEMPLATE.setProperty("Project", autoRefresh);
        PropertyMaps.TEMPLATE.setProperty("Project", excludedFiles);
        PropertyMaps.TEMPLATE.setProperty("Project",
                new FileListProperty("project.extra.class.path", File.pathSeparator, DEF_DIR,
                        "Returns a list of files in the project's extra "+
                                "class path.\n"+
                                "Optional attributes:\n"+
                                "\trel=\"<dir to which output should be relative\"\n"+
                                "\tsep=\"<separator between files>\"\n"+
                                "\tsquote=\"<true to enclose file in single quotes>\"\n"+
                                "\tdquote=\"<true to enclose file in double quotes>\"") {
                    protected List<File> getList(PropertyMaps pm) {
                        ArrayList<File> l = new ArrayList<File>();
                        for(File f: _model.getExtraClassPath()) { l.add(f); }
                        return l;
                    }
                    public String getLazy(PropertyMaps pm) { return getCurrent(pm); }
                    public boolean isCurrent() { return false; }
                });

        // Actions
        PropertyMaps.TEMPLATE.setProperty("Action", new DrJavaActionProperty("action.save.all", "(Save All...)", "Execute a \"Save All\" action.") {
            public void update(PropertyMaps pm) { _saveAll(); }
            public boolean isCurrent() { return false; }
        });

        PropertyMaps.TEMPLATE.setProperty("Action", new DrJavaActionProperty("action.compile.all", "(Compile All...)","Execute a \"Compile All\" action.") {
            public void update(PropertyMaps pm) { _compileAll(); }
            public boolean isCurrent() { return false; }
        });

        PropertyMaps.TEMPLATE.setProperty("Action", new DrJavaActionProperty("action.clean", "(Clean Build Directory...)","Execute a \"Clean Build Directory\" action.") {
            public void update(PropertyMaps pm) {
                // could not use _clean(), since ProjectFileGroupingState.cleanBuildDirectory()
                // is implemented as an asynchronous task, and DrJava would not wait for its completion
                IOUtil.deleteRecursively(_model.getBuildDirectory());
            }
            public boolean isCurrent() { return false; }
        });


        PropertyMaps.TEMPLATE.setProperty("Action", getDrJavaActionProperty(DEF_DIR));
        PropertyMaps.TEMPLATE.setProperty("Action", new DrJavaActionProperty("action.auto.refresh", "(Auto-Refresh...)","Execute an \"Auto-Refresh Project\" action.") {
            public void update(PropertyMaps pm) {
                _model.autoRefreshProject();
            }
            public boolean isCurrent() { return false; }
        });
    }

    private FileListProperty getProjectFiles(final String DEF_DIR) {
        FileListProperty projectFiles = new FileListProperty("drjava.project.files", File.pathSeparator, DEF_DIR,"Returns a list of all files open in DrJava that belong " +
                "to a project and are underneath the project root.\n" +
                "Optional attributes:\n" +
                "\trel=\"<dir to which output should be relative\"\n" +
                "\tsep=\"<separator between files>\"\n"+
                "\tsquote=\"<true to enclose file in single quotes>\"\n"+
                "\tdquote=\"<true to enclose file in double quotes>\"") {
            protected List<File> getList(PropertyMaps pm) {
                ArrayList<File> l = new ArrayList<File>();
                for(OpenDefinitionsDocument odd: _model.getProjectDocuments()) {
                    l.add(odd.getRawFile());
                }
                return l;
            }
            public String getLazy(PropertyMaps pm) { return getCurrent(pm); }
            public boolean isCurrent() { return false; }
        };
        return projectFiles;
    }

    private FileListProperty getAllFiles(final String DEF_DIR) {
        FileListProperty allFiles = new FileListProperty("drjava.all.files", File.pathSeparator, DEF_DIR,
                "Returns a list of all files open in DrJava.\n"+
                        "Optional attributes:\n"+
                        "\trel=\"<dir to which output should be relative\"\n"+
                        "\tsep=\"<separator between files>\"\n"+
                        "\tsquote=\"<true to enclose file in single quotes>\"\n"+
                        "\tdquote=\"<true to enclose file in double quotes>\"") {
            protected List<File> getList(PropertyMaps pm) {
                ArrayList<File> l = new ArrayList<File>();
                for(OpenDefinitionsDocument odd: _model.getOpenDefinitionsDocuments()) {
                    l.add(odd.getRawFile());
                }
                return l;
            }
            public String getLazy(PropertyMaps pm) { return getCurrent(pm); }
            public boolean isCurrent() { return false; }
        };
        return allFiles;
    }

    private DrJavaProperty getProjectMode() {
        DrJavaProperty projectMode = new DrJavaProperty("project.mode",
                "Evaluates to true if a project is loaded.") {
            public void update(PropertyMaps pm) {
                Boolean b = _model.isProjectActive();
                String f = _attributes.get("fmt").toLowerCase();
                if (f.equals("int")) _value = b ? "1" : "0";
                else if (f.equals("yes")) _value = b ? "yes" : "no";
                else _value = b.toString();
            }
            public String getLazy(PropertyMaps pm) { return getCurrent(pm); }
            public void resetAttributes() {
                _attributes.clear();
                _attributes.put("fmt", "boolean");
            }
            public boolean isCurrent() { return false; }
        };
        return projectMode;
    }

    private DrJavaProperty getProjectChanged() {
        DrJavaProperty projectChanged = new DrJavaProperty("project.changed",
                "Evaluates to true if the project has been changed since the last save.") {
            public void update(PropertyMaps pm) {
                String f = _attributes.get("fmt").toLowerCase();
                Boolean b = _model.isProjectChanged();
                if (f.equals("int")) _value = b ? "1" : "0";
                else if (f.equals("yes")) _value = b ? "yes" : "no";
                else  _value = b.toString();
            }
            public String getLazy(PropertyMaps pm) { return getCurrent(pm); }
            public void resetAttributes() {
                _attributes.clear();
                _attributes.put("fmt", "boolean");
            }
            public boolean isCurrent() { return false; }
        };
        return projectChanged;
    }

    private FileProperty getMasterWorkingDir() {
        FileProperty masterWorkingDir =  new FileProperty("drjava.master.working.dir", new Thunk<File>() {
            public File value() { return _model.getMasterWorkingDirectory(); }
        },
                "Returns the working directory of the DrJava master JVM.\n"+
                        "Optional attributes:\n"+
                        "\trel=\"<dir to which output should be relative\"\n"+
                        "\tsquote=\"<true to enclose file in single quotes>\"\n"+
                        "\tdquote=\"<true to enclose file in double quotes>\"") {
            public String getLazy(PropertyMaps pm) { return getCurrent(pm); }
        };
        return masterWorkingDir;
    }

    private FileProperty getWorkingDir() {
        return new FileProperty("drjava.working.dir", new Thunk<File>() {
            public File value() {
                return _model.getInteractionsModel().getWorkingDirectory();
            }
        },
                "Returns the current working directory of DrJava.\n" +
                        "Optional attributes:\n" +
                        "\trel=\"<dir to which output should be relative\"\n" +
                        "\tsquote=\"<true to enclose file in single quotes>\"\n" +
                        "\tdquote=\"<true to enclose file in double quotes>\"") {
            public String getLazy(PropertyMaps pm) {
                return getCurrent(pm);
            }
        };
    }

    private DrJavaProperty getCurrentCol() {
        DrJavaProperty currentCol = new DrJavaProperty("drjava.current.col",
                "Returns the current column in the Definitions Pane.") {
            public void update(PropertyMaps pm) {
                _value = String.valueOf(_posListener.lastCol());
            }
            public String getLazy(PropertyMaps pm) { return getCurrent(pm); }
            public boolean isCurrent() { return false; }
        };
        return currentCol;
    }

    private DrJavaProperty getCurrentLine() {
        DrJavaProperty currentLine = new DrJavaProperty("drjava.current.line", "Returns the current line in the Definitions Pane.") {
            public void update(PropertyMaps pm) { _value = String.valueOf(_posListener.lastLine()); }
            public String getLazy(PropertyMaps pm) { return getCurrent(pm); }
            public boolean isCurrent() { return false; }
        };
        return currentLine;
    }

    private DrJavaActionProperty getDrJavaActionProperty(final String DEF_DIR) {
        DrJavaActionProperty actionProperty = new DrJavaActionProperty("action.open.file", "(Open File...)","Execute an \"Open File\" action.\n"+"Required attributes:\n"+"\tfile=\"<file to open>\"\n"+"Optional attributes:\n"+"\tline=\"<line number to display>") {
            public void update(PropertyMaps pm) {
                if (_attributes.get("file") != null) {
                    final String dir = StringOps.unescapeFileName(StringOps.replaceVariables(DEF_DIR, pm, PropertyMaps.GET_CURRENT));
                    final String fil = StringOps.unescapeFileName(StringOps.replaceVariables(_attributes.get("file"), pm, PropertyMaps.GET_CURRENT));
                    FileOpenSelector fs = new FileOpenSelector() {
                        public File[] getFiles() {
                            if (fil.startsWith("/")) { return new File[] { new File(fil) }; }
                            else { return new File[] { new File(dir, fil) }; }
                        }
                    };
                    open(fs);
                    int lineNo = -1;
                    if (_attributes.get("line") != null) {
                        try { lineNo = Integer.valueOf(_attributes.get("line")); }
                        catch(NumberFormatException nfe) { lineNo = -1; }
                    }
                    if (lineNo >= 0) {
                        final int l = lineNo;
                        Utilities.invokeLater(new Runnable() { public void run() { _jumpToLine(l); } });
                    }
                }
            }
            /** Reset the attributes. */
            public void resetAttributes() {
                _attributes.clear();
                _attributes.put("file", null);
                _attributes.put("line", null);
            }
            public boolean isCurrent() { return false; }
        };
        return actionProperty;
    }

    private FileProperty getCurrentProperty() {
        String help = "Returns the current document in DrJava.\n" +
                "Optional attributes:\n" +
                "\trel=\"<dir to which the output should be relative\"\n" +
                "\tsquote=\"<true to enclose file in single quotes>\"\n" +
                "\tdquote=\"<true to enclose file in double quotes>\"";

        return new FileProperty("drjava.current.file", new Thunk<File>() {
            public File value() {
                return _model.getActiveDocument().getRawFile();
            }
        }, help) {
            public String getLazy(PropertyMaps pm) { return getCurrent(pm); }
        };
    }
}
