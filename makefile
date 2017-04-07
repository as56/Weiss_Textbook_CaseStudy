JFLAGS = -processor index
JC = ${CHECKERFRAMEWORK}/checker/bin-devel/javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java



#Files I was unable to annotate
#BinomialQueue.java -- lots of weird cases, I dont full understand this code
#SuffixArray.java --lots of weird cases with external dependencies, confused on how to annotate
#RadixSort.java -- this file has compilation problems I do not know how to resolve

CLASSES = \
  AvlTree.java \
  BinaryHeap.java \
  BinarySearchTree.java \
  CuckooHashTable.java \
  DisjSets.java \
  Fig01_02.java \
  Fig01_03.java \
  Fig01_04.java \
  Fig02_09.java \
  Fig02_10.java \
  Fig02_11.java \
  Fig02_12.java \
  Fig10_38.java \
  Fig10_40.java \
  Fig10_43.java \
  Fig10_43.java \
  Fig10_45.java \
  Fig10_53.java \
  HashFamily.java \
  IntCell.java \
  KdTree.java \
  LeftistHeap.java \
  MaxSumTest.java \
  MemoryCell.java \
  MyArrayList.java \
  MyLinkedList.java \
  PairingHeap.java \
  QuadraticProbingHashTable.java \
  Random.java \
  RedBlackTree.java \
  RemoveEvens.java \
  SeparateChainingHashTable.java \
  Sort.java \
  SplayTree.java \
  StringHashFamily.java \
  TestIntCell.java \
  TestMemoryCell.java \
  Treap.java \
  WordLadder.java \


default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
