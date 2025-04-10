# J-UML-Editor
A Java-based UML editor for creating flow and object diagrams with linking, labeling, and grouping.

![image](https://github.com/user-attachments/assets/1d4b38ae-dbec-4834-b259-7fc7e0c6b4fb)

Project features：
1. Creating an object (rect / ova)
2. Creating a Link
3. Select / Unselect a single objects
4. Group objects
5. Move objects
6. Customize Label Style

Project Structure：

src/
└── main/
    └── java/
        └── editor/
            ├── canvas/
            │   └── CanvasPanel.java         
            ├── controller/
            │   └── EditorController.java    
            ├── model/
            │   ├── GraphicalObject.java     
            │   ├── RectObject.java          
            │   ├── OvalObject.java          
            │   ├── CompositeObject.java     
            │   ├── Link.java                
            │   └── LinkType.java            
            ├── ui/
            │   ├── MainFrame.java           
            │   └── LabelDialog.java         
            └── Main.java                    

