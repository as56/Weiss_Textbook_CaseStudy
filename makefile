JFLAGS = -processor index
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java



#Files I was unable to annotate
#BinomialQueue.java -- lots of weird cases, I dont full understand this code
#RadixSort.java -- this file has compilation problems I do not know how to resolve

CLASSES = \
	Fig01_02.java \
	Fig01_03.java \
        Fig01_04.java \
        IntCell.java \
	MemoryCell.java\
	TestIntCell.java\
	TestMemoryCell.java\
	Fig02_10.java \
        Fig02_11.java \
	Fig02_12.java \
	RemoveEvens.java\
	MyLinkedList.java\
	BinarySearchTree.java\
	AvlTree.java\
	WordLadder.java\
	LeftistHeap.java\
	Fig10_40.java \
        Fig10_43.java \
	SplayTree.java\
	RedBlackTree.java\
	MyArrayList.java\
	SeparateChainingHashTable.java\
	QuadraticProbingHashTable.java\
	CuckooHashTable.java\
	HashFamily.java\
	StringHashFamily.java\
	MaxSumTest.java\
	Fig02_09.java \
	DisjSets.java\
        Fig10_38.java \
	Fig10_45.java \
	BinaryHeap.java\
        KdTree.java\
        PairingHeap.java\
        Random.java\
        Treap.java\
        Fig10_53.java\
	Fig10_43.java\
	Sort.java\
	
	
	


default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
