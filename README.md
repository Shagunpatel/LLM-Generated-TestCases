Defects4J -- version 3.0.1 [![.github/workflows/ci.yml](https://github.com/rjust/defects4j/actions/workflows/ci.yml/badge.svg)](https://github.com/rjust/defects4j/actions/workflows/ci.yml)
================
Defects4J is a collection of reproducible bugs and a supporting infrastructure
with the goal of advancing software engineering research.


Setting up Defects4J
================

Requirements
----------------
 - Java 11
 - Git >= 1.9
 - Subversion (svn) >= 1.8
 - Perl >= 5.0.12
 - `cpanm`
  


#### Perl dependencies
All required Perl modules are listed in [cpanfile](https://github.com/rjust/defects4j/blob/master/cpanfile).
On many Unix platforms, these required Perl modules are installed by default.
The setup instructions immediately below install them if necessary.
If you do not have `cpanm` installed, use cpan or a cpan wrapper to install the perl modules listed in `cpanfile`.

Steps to set up Defects4J
----------------

1. Clone Defects4J:
    - `git clone https://github.com/rjust/defects4j`

2. Initialize Defects4J (download the project repositories and external libraries, which are not included in the git repository for size purposes and to avoid redundancies):
    - `cd defects4j`
    - `cpanm --installdeps .`
    - `./init.sh`

3. Add Defects4J's executables to your PATH:
    - `export PATH=$PATH:"path2defects4j"/framework/bin`
    ("path2defects4j" points to the directory to which you cloned Defects4J; it
     looks like "/user/yourComputerUserName/desktop/defects4j".)

4. Check installation:
    - `defects4j info -p Lang`

On some platforms such as Windows, you might need to use `perl "fullpath"\defects4j`
where these instructions say to use `defects4j`.


Using Defects4J
================

#### Example commands
1. Get information for a specific project (commons lang):
    - `defects4j info -p Lang`

2. Get information for a specific bug (commons lang, bug 1):
    - `defects4j info -p Lang -b 1`

3. Checkout a buggy source code version (commons lang, bug 1, buggy version):
    - `defects4j checkout -p Lang -v 1b -w /tmp/lang_1_buggy`

4. Change to the working directory, compile sources and tests, and run tests:
    - `cd /tmp/lang_1_buggy`
    - `defects4j compile`
    - `defects4j test`




License
---------
MIT License, see [`license.txt`](https://github.com/rjust/defects4j/blob/master/license.txt) for more information.
