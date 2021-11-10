basic-tutorial.md

basicTutorial

# Accessing and Modifying the Source Code
    
    <para>Now that your development system is ready, you can get to work!
    These instructions will demonstrate some of the typical tasks you'll want
    to accomplish in browsing, building, and modifying the DrJava
    sources.</para>
    
## Downloading the Sources
      
      <para>You can use Subversion or Git to get a copy of the DrJava source
      code. In Subversion terminology, downloading a fresh copy is (usually)
      synonymous with "checking out" the code; the corresponding operation with
      Git is "cloning" the repository. You can download the sources with the
      following commands:
      
        <informalexample><para><literal>$ svn co
        https://drjava.svn.sourceforge.net/svnroot/drjava/trunk/drjava</literal></para></informalexample>

        <informalexample><para><literal>$ git clone  https://github.com/DrJavaAtRice/drjava.git</literal></para></informalexample>
        
        A <filename>drjava</filename> directory will be created in your working
        directory. Subversion will output the name of each file it downloads,
        while Git will give you progress reports along the way.</para>
      
      <para>Note that the above Subversion command only checks out
      <emphasis>part</emphasis> of the DrJava sources. The sources are divided
      into independent modules, each a subdirectory of
      <filename>trunk</filename>. Each module can be built, tested, and
      modified without the others. You can check out a different directory by
      simply modifying the URL given to Subversion. Git works on a different
      model, and as such cannot clone only part of a repository; the above Git
      command will include all of the sources.
      
        <warning> <para>While it is possible to check out the entire
        <filename>https://drjava.svn.sourceforge.net/svnroot/drjava</filename>
        directory, it is not necessary. This top-level directory contains
        dozens of copies of the sources, including snapshots at various stages
        of development.  If you download this directory, it will take a lot of
        time and disk space.</para> </warning>
       
        The full list of <filename>trunk</filename>'s subdirectories includes:
      
        <variablelist> <varlistentry> <term><literal>drjava</literal></term>
        <listitem><para>The main application, containing the bulk of the code.
        Building this module will create the DrJava
        application.</para></listitem> </varlistentry>
          
          <varlistentry> <term><literal>dynamicjava</literal></term>
          <listitem><para>The DynamicJava interpreter, which implements the
          functionality behind the Interactions Pane.</para></listitem>
          </varlistentry>
          
          <varlistentry> <term><literal>javalanglevels</literal></term>
          <listitem><para>A Java preprocessor used to provide the Language
          Levels facility.</para></listitem> </varlistentry>
          
          <varlistentry> <term><literal>plt</literal></term>
          <listitem><para>General-purpose utility classes, including things
          that are "missing" from the Java API like predicates and
          tuples.</para></listitem> </varlistentry>
          
          <varlistentry> <term><literal>platform</literal></term>
          <listitem><para>A collection of platform-dependent code, such as the
          concrete compiler interfaces and special OS-specific GUI setup
          instructions.</para></listitem> </varlistentry>
          
          <varlistentry> <term><literal>docs</literal></term>
          <listitem><para>The project documentation, including this document
          and end-user help files.</para></listitem> </varlistentry>
          
          <varlistentry> <term><literal>eclipse</literal></term>
          <listitem><para>A wrapper for the interactions pane that allows it to
          be run as a plug-in to the Eclipse IDE.</para></listitem>
          </varlistentry>
          
          <varlistentry> <term><literal>jedit</literal></term>
          <listitem><para>A similar wrapper for the interactions pane in the
          JEdit IDE.</para></listitem> </varlistentry>

          <varlistentry> <term><literal>misc</literal></term>
          <listitem><para>Files that don't belong in a specific
          module.</para></listitem> </varlistentry>

        </variablelist></para>
        
      <para>You may find it helpful to streamline the check-out process by
      creating an alias or a script file for the check-out command.</para>
      
    
## Building the Sources
      
      <para>In the <filename>drjava</filename> directory, you will find the
      file <filename>build.xml</filename>.  This is the module's Ant script,
      containing instructions that automate a variety of development tasks.
      While in the <filename>drjava</filename> directory, enter:
      
        <informalexample><para><literal>ant
        help</literal></para></informalexample>
        
        or just
    
        <informalexample><para><literal>ant</literal></para></informalexample>
        
        The name <literal>help</literal> refers to an Ant
        <firstterm>target</firstterm>.  Different targets can be used to
        accomplish different tasks, and they are often set up to recognize
        dependencies.  For example, the <literal>test</literal> target
        recognizes its dependency on the <literal>compile</literal> target;
        when you ask Ant to run the tests, it will automatically compile them
        first.  In this particular example, the <literal>help</literal> target
        (which is set up as the default when none is specified) simply prints a
        message about the script and the environment settings it expects.  If
        an error occurs here, Ant may not be properly set up.</para>
        
      <para>Second, enter the following command:
        
        <informalexample><para><literal>ant
        -p</literal></para></informalexample>
        
        You will see a list of all the documented targets in the project.  Note
        that, due to the dependencies between targets, the
        <literal>build</literal> target will run
        <literal>generate-source</literal>, <literal>compile</literal>,
        <literal>test</literal>, and <literal>jar</literal>.  That is, it will
        do everything required to build and test a new application.  Try it
        out:
        
        <informalexample><para><literal>ant
        build</literal></para></informalexample>
        
        The process will take awhile.  Ant will generally log each action it
        takes to the console; you should make sure you can follow what's going
        on.  Here's a summary of the major steps:
        
        <itemizedlist>
        
          <listitem> <formalpara>
          <title><literal>generate-source</literal></title> <para>Performs any
          necessary preprocessing before the Java compiler is invoked.  For
          example, a <filename>Version.java</filename> file is created so that
          a unique version number for this build will be accessible to the code
          (in particular, the DrJava application's <guilabel>About</guilabel>
          dialog box).</para> </formalpara> </listitem>
        
          <listitem> <formalpara> <title><literal>do-compile</literal></title>
          <para>Invokes the <command>javac</command> compiler (the specific
          version used depends on your command path).  Generated class files
          will be placed in a new <filename>classes</filename> directory, with
          the subdirectories <filename>base</filename> and
          <filename>test</filename> for standard and test classes,
          respectively.  Any compiler errors or warnings will be printed to the
          console.  Where warnings are expected, they will be tagged in the
          code with a <literal>@SuppressWarnings</literal> annotation.  Thus,
          any warnings you see during compilation highlight a problem that
          should be addressed.</para> </formalpara> </listitem>

          <listitem> <formalpara> <title><literal>unjar-libs</literal></title>
          <para>Expands the <filename>*.jar</filename> files in the
          <filename>lib</filename> directory into
          <filename>classes/lib</filename>.  These library classes will be
          bundled with the finished product.</para> </formalpara> </listitem>
        
          <listitem> <formalpara> <title><literal>test</literal></title>
          <para>Runs the JUnit tests in <filename>classes/test</filename> (the
          specific version of <command>java</command> used depends on your
          command path).  This will constitute the bulk of the build time.  A
          summary of the running time for each test will be logged, and if a
          failure occurs, testing will halt immediately.</para> </formalpara>
          </listitem>
        
          <listitem> <formalpara> <title><literal>jar</literal></title>
          <para>Creates the <filename>drjava.jar</filename> file.  This is the
          executable application.  It will contain a
          <filename>MANIFEST.MF</filename> file listing the build's unique
          version number and your user name.</para> </formalpara> </listitem>
          </itemizedlist> </para>
      
      <para>Now that you've built the application, you can run it with the
      following command:
        
        <informalexample><para><literal>java -jar
        drjava.jar</literal></para></informalexample>
        
        You can also run the application in some systems by double-clicking on
        the <filename>drjava.jar</filename> file.  And the Ant script includes
        a variety of <literal>run</literal> commands that act as shortcuts:
        <literal>ant run-jar</literal>, for example, will compile, build a jar
        file, and run the application with assertions and error logging turned
        on.</para>
          
      <note> <title>Mac OS X</title> <para>When you run the
      <filename>drjava.jar</filename> file, the DrJava GUI may not look quite
      right.  This is because the official OS X application release wraps the
      jar file in some additional packaging and settings.  However, all the
      essential functionality should still be there in your unofficial
      version.</para> </note> </section>
    
## Modifying the Sources
      
      <para>Once you've got a fresh copy of the sources and verified that they
      will build correctly, you're ready to start making modifications.  The
      source files are stored in the <filename>src</filename> directory; you
      should be able to explore and edit them in any IDE or text editor.  IDEs
      will be able to interface with the build script with various degrees of
      sophistication.  If the IDE you use does not support Ant scripts (or
      you'd rather not bother with making it work properly), you can either do
      most of your building and testing from the command line, or do your
      "casual" development in the IDE, and just run the scripts when you are
      ready to commit a change.  In the latter case, you'll want to keep a few
      things in mind:
      
        <itemizedlist> <listitem><para>It's best to consider the contents of
        the <literal>drjava</literal> directory transient &mdash; you will want
        to occasionally delete the directory completely and start from a fresh
        checkout.  Thus, IDE-specific files like project descriptions are
        better stored somewhere else (unless you don't mind recreating
        them).</para></listitem>
          
          <listitem><para>By default, the <command>javac</command> compiler
          places the <filename>*.class</filename> files it generates in the
          same location as their sources.  That approach clutters up the
          <filename>src</filename> directory significantly, and should be
          avoided.  You should also avoid putting the compiled classes in one
          of the Ant script's target locations (such as
          <filename>classes/base</filename>), as that might lead to confusing
          behavior when you invoke the script later.  The best option is to
          either create a
          <filename>classes/<replaceable>ide-name</replaceable></filename>
          directory in which to place your classes, or just store the
          IDE-compiled classes somewhere else entirely.</para></listitem>
          
          <listitem><para>Your IDE's classpath should contain all the jar files
          in the <filename>lib</filename> directory (but not the
          <filename>lib/buildlib</filename> directory, with the exception of
          <filename>lib/buildlib/junit.jar</filename>).</para></listitem>
          
          <listitem><para>You'll need to invoke the
          <literal>generate-source</literal> Ant target before you attempt to
          compile in the IDE (that is, unless the files have already been
          generated).  Otherwise, some sources will be missing and the
          compilation will fail.  Some IDEs may also have trouble with these
          files occasionally disappearing and reappearing, so you might want to
          ensure that the sources are in a consistent state before opening or
          closing the IDE application.</para></listitem>
          
        </itemizedlist> If you're using Eclipse, see the <link
        linkend="eclipse">Eclipse section</link> for instructions on setting up
        a project.</para>
      
      <para>To get oriented and understand the program design, you may want to
      browse the <link linkend="systemArchitecture">System Architecture</link>
      notes, along with the javadocs from the latest release (available at
      <ulink url="http://drjava.org">drjava.org</ulink>).  You can also
      generate your own up-to-date copy of the javadocs by invoking the Ant
      <literal>javadoc</literal> target.  Before you make significant changes
      to the code, you should familiarize yourself with the <link
      linkend="bestPractices">Development Best Practices</link> section of this
      document.</para>
      
      <para>Once you've made some modifications, you'll probably want to try
      them out.  The Ant script offers a couple of ways to do this.  First, you
      can enter
    
        <informalexample><para><literal>ant
        run</literal></para></informalexample>
      
        This will run the DrJava application located in the
        <filename>classes/base</filename> directory.  You can also run the
        JUnit tests.  You can run a <emphasis>specific</emphasis> test (rather
        than waiting for <emphasis>all</emphasis> of them to run) by typing
        
        <informalexample><para><literal>ant
        -Dtest-spec=<replaceable>filterString</replaceable>
        test</literal></para></informalexample>
        
        All test classes in <filename>classes/test</filename> with paths
        matching <replaceable>filterString</replaceable> will be run by
        JUnit.</para>
        
    </section>
    
# Submitting Your Changes
      
      <para>When you're ready to submit the changes you've made to the
      Subversion archive (in Subversion terminology,
      <firstterm>commit</firstterm> the changes), and assuming you're a member
      of the DrJava SourceForge project, you can do so with the Ant script. If
      you're a Git user you will have to <command>commit</command> and
      <command>push</command> manually, or if you don't have write access to
      the repository, submit a <ulink
      url="https://help.github.com/articles/about-pull-requests/">pull
      request.</ulink></para>
      
      <para>While the <command>svn</command> command could be
      invoked directly from the command line (or some IDEs), using Ant is the
      preferred approach because it allows you to ensure that your changes do
      not break any functionality or conflict with other changes that have been
      made by other developers concurrently. The Ant script will run a fresh
      compile and all tests before committing your changes.</para>
      
      <para>If this is the first time you've committed a change on your current
      system, you will need to perform a manual commit in order to check your
      authentication.  You can do this by checking out the
      <filename>https://drjava.svn.sourceforge.net/svnroot/drjava/trunk/misc/authenticate</filename>
      directory and following the instructions in
      <filename>authenticate/authenticate.txt</filename>.  When you commit, you
      will be prompted for a password (if the username is incorrect, just hit
      Enter and you will be prompted for a username as well).  [TODO: Improve
      this process, if possible.  It would be nice if there were just an "svn
      authenticate" command.]  That authentication information will be stored
      on your system, and future commits will not require you to reenter your
      password.</para>
      
      <para>After your system is set up for automatic authentication, you can
      perform a commit with the following:

        <informalexample><para><literal>ant
        commit</literal></para></informalexample>
        
        The major steps in this process are enumerated below:
        
        <itemizedlist>
        
          <listitem> <formalpara>
          <title><literal>clean-intermediate</literal></title> <para>Removes
          all intermediate build products &mdash; that is, files that didn't
          come from the Subversion repository and that aren't finished
          products.  This will include generated sources and the
          <filename>classes</filename> directory.  Note that, typically, this
          target (or just <literal>clean</literal>) is also invoked directly as
          the need arises.</para> </formalpara> </listitem>
        
          <listitem> <formalpara>
          <title><literal>clean-products</literal></title> <para>Removes all
          final build products.  This will include DrJava jar files and
          generated javadocs.</para> </formalpara> </listitem>
        
          <listitem> <formalpara> <title><literal>update</literal></title>
          <para>Downloads all new changes that have been made in the Subversion
          repository since you last checked out (or updated) your sources.  The
          command will list all filenames that are being changed locally; after
          the update, the <literal>status</literal> Subversion command will
          display any discrepancies between the repository and your working
          copy.  If there is a conflict in the update (you and someone else
          have both changed the same file, or perhaps specifically an
          overlapping part of the same file), Subversion will let you know and
          give you a chance to manually merge the changes.  This target is also
          typically invoked directly as the need arises (and you should use it
          <emphasis>often</emphasis> to catch conflicts while they are still
          small).</para> </formalpara> </listitem>
        
          <listitem> <formalpara> <title><literal>build</literal></title>
          <para>The project is built from scratch, as described previously.
          This ensures that your submission is a valid, tested copy of the
          program.</para> </formalpara> </listitem>
        
          <listitem> <formalpara>
          <title><literal>clean-intermediate</literal></title> <para>To prevent
          extraneous messages when the commit takes place, the project is
          cleaned up once again.</para> </formalpara> </listitem>
        
          <listitem> <formalpara> <title><literal>commit</literal></title>
          <para>You are first prompted to enter a log message describing the
          changes you've made.  These are generally just one-line summaries.
          Keep in mind that your message will be read in the context of the
          entire project when, for example, someone wants to know what changes
          have been made to the application recently.  If you need help in
          remembering what files you've touched, look at the output of
          <literal>update</literal>, which ran just before the project was
          rebuilt. Next, each file you've modified will be uploaded and the
          changes will be assigned a fresh revision number.</para>
          </formalpara> </listitem>
        
        </itemizedlist> </para> </section>
        
  </section> 

</chapter>
