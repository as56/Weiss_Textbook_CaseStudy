JFLAGS = -processor index
JC = ${CHECKERFRAMEWORK}/checker/bin-devel/javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

# To typecheck just one file:
#   make SuffixArray.class


#Files I was unable to annotate
#SuffixArray.java --lots of weird cases with external dependencies, confused on how to annotate

JAVA_FILES = $(wildcard *.java)

default: typecheck

# Typechecks all files, with one invocation of javac
typecheck:
	$(JC) $(JFLAGS) -proc:only *.java

# Typechecks all files, with a different invocation of java for each file
classes: $(JAVA_FILES:.java=.class)

clean:
	$(RM) *.class
