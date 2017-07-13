Execution:
           Open  WSD.jar, then the WSD system is used by one of the following:
                1-The following actions must be performed to use the system to get a sense of an one word:
                   1.Enter a text in The text box.  You can enter the text by one of the following two way:
                      a.Write your text in the field.
                        b.Take the text from an existing file through Open menu item in File menu.
                   2.Select your target word by highlighting it.
                   3.Press the Find sense button; the result will be displayed in a bottom of the GUI.
                2-The following actions must be performed to test WSD system by using standard corpora:
                    1.Select Test menu item in Corpora menu from menu bar. 
                    2.Enter a number of runs, in runs box.
                    3.Choose which the corpora that you want to test the system by it:
                       a.SensEval-2: task#1, fine-grained English All-Words (S2FGAW).
                       b.SensEval3: task#1, fine-grained English All-Words (S3FGAW).
                       c.SemEval2007: Task#17 Subtask#3, fine-grained English All-Words (S07FGAW).
                       d.SemEval2007: Task#7; coarse-grained English All-Words (S07CGAW)).
                    4.The result of the WSD system will be displayed it in the Score folder where:
                       a.The Text file is the answer with ID of target word.
                       b.The Excel file is the score ( 2 sheets: one for each word and another for over all word).
                There are other utilities in AWSD system, such as:
                    1.You can save your text and the result in new file through Save menu item in File menu from menu bar.
                    2.You can clear all the fields in the GUI through press the clear button
.......................................
Folders:
1- dict1.7, dict2.0, dict2.1, and dict3.0:        The WordNet dataset virsion 1.7,2.0, 2.1, and 3.0.
2- WNPP:    The WordNet ++ dataset
3- SemEval1707, SemEval2007, SensEval2, SensEval3:      The corpora.
4- Support2, Support3, Support1707, and Support2007:   They content on the files for the preprocess of the SemEval1707, SemEval2007, SensEval2, SensEval3 corpra
5- SourceCode:   the code of the algorithm
6- Score: The result of the GAWSD system where:
     1- The Text file is the answer with ID of target word.
     2- The Excel file is the score ( 2 sheets: one for each word and another for over all word)