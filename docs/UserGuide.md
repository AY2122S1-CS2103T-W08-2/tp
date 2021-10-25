---
layout: page
title: User Guide
---

Source Control is a **desktop app for CS1101S professors to manage the performance of their students, optimized for use via a Command Line Interface** (CLI) while still having the benefits of a Graphical User Interface (GUI). Source Control can give you both a quick overview and a closer look of how your students are doing.

* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## Quick start

1. Ensure you have Java `11` or above installed in your Computer.

1. Download the latest `sourcecontrol.jar` from [here](https://github.com/AY2122S1-CS2103T-W08-2/tp/releases).

1. Copy the file to the folder you want to use as the _home folder_ for Source Control.

1. Double-click the file to start the app. The GUI similar to the below should appear in a few seconds. Note how the app contains some sample data.<br>
   ![Ui](images/Ui.png)

1. Type the command in the command box and press Enter to execute it. e.g. typing **`help`** and pressing Enter will open the help window.<br>
   Some example commands you can try:

    * **`add student`**`-n John Doe -i E0123456` : Adds a student named `John Doe` with NUSNET ID `E0123456` into the database.

    * **`add group`**`-g T01A -n John Doe` : Adds a group called `T01A` into the database and student `John Doe` into the group.

    * **`add alloc`**`-g T01A -n John Doe` : Adds student `John Doe` into group `T01`.

    * **`add score`**`-a P01 -n John Doe -s 12`: Adds score for assessment `P01` as `12` for student `John Doe`.

    * **`search`**`-n John Doe` : Searches for student `John Doe`.

    * **`clear`** : Clears all existing data.
1. Refer to the [Features](#features) below for details of each command.

--------------------------------------------------------------------------------------------------------------------

## Features

<div markdown="block" class="alert alert-info">

**:information_source: Notes about the command format:**<br>

* Words in `<angled brackets>` are the parameters to be supplied by the user.<br>
  e.g. in `add -n <student_name>`, `<student_name>` is a placeholder which can be used as `add -n John Doe`.

* Parameters in `(round brackets)` separated by `|` are mutually exclusive options for input. Only one input should to be supplied by the user.<br>
  e.g. `(-n <student_name> | -i <student_id> | -group <group_name>)` can be used as `-n John Doe`, or as `-i E0123456`, or as `-g T02A`

* Parameters in `[square brackets]` are optional. <br>
  e.g. `-n <student_name> [-g <group_name>]` can be used as `-n John Doe -g T01A`, or as `-n John Doe`.

* Items with `...​`  after them can be used multiple times including zero times.<br>
  e.g. `[-g <group_name>]...` can be used as ` ` (i.e. 0 times), or `-g T01A -g R01A`.

* Parameters can be in any order.<br>
  e.g. if the command specifies `-n <student_name> -g <group_name>`, then `-g <group_name> -n <student_name>` is also acceptable.

* If a parameter is expected only once in the command but you specify it multiple times, only the last occurrence of the parameter will be taken.<br>
  e.g. if you specify `-g T02A -g T03B` and the command only expects one group, only `-g T03B` will be taken.

* Extraneous parameters for commands that do not take in parameters (such as `clear`) will be ignored.<br>
  e.g. if the command specifies `clear 123`, it will be interpreted as `clear`.

</div>

### Viewing help : `help`

Shows a message explaining how to access the help page.

![help message](images/helpMessage.png)

Format: `help`

### Listing all persons : `list`

Shows a list of all persons in the address book.

Format: `list`


### Adding a student : `add student`

Adds a student into the database.

Format: `add student -n <student_name> -i <student_id>  [-g <group_name>] [-t <tag_name>]...`

* Adds a new student with the given name and NUSNET ID into the database.
* Adds the student into the groups the student belongs to.
* Adds a tag to the student if applicable.

Examples:
* `add student -n John Doe -i E0123456`
* `add student -n Jane Doe -i E0123457 -g T01A -g R01A`
* `add student -n Jane Doe -i E0123458 -t beginner`

### Creating a new group: `add group`

Creates a new group and adds students into the group.

Format: `add group -g <group_name> [(-n <student_name> | -i <student_id>)]...`

* Creates a new group with the given group name.
* Students can be identified by their name or NUSNET ID.

Examples:
* `add group -g FG1 -n John Doe -n Jane Doe`
* `add group -g FG1`
* `add group -g FG1 -n John Doe -i E0123456`
* `add group -g FG1 -i E0123123 -i E0123456`

### Adding a student into a group: `add alloc`

Allocates an existing student into an existing group.

Format: `add alloc -g <group_name> (-n <student_name> | -i <student_id>)`
* Adds the student into an existing group specified by the group name.
* Students can be identified by their name or NUSNET ID.

Examples:
* `add alloc -g T01A -n John Doe`
* `add alloc -g T02A -i E0123456`

### Adding an assessment: `add assessment`

Adds a new assessment into the database.

Format: `add assessment -a <assessment_name>`
* Adds an assessment only if the assessment is not already in the database.
* Assessment name can only be alphanumeric.

Examples:
* `add assessment -a P01`

### Adding a score: `add score`

Adds score of an existing assessment into the database.

Format: `add score -a <assessment_name> (-n <student_name> | -i <student_id>) -s <score>`
* Adds student’s score for an existing assessment into the database.
* Updates the student's score if the student already has a score for the assessment.
* Students can be identified by their name or NUSNET ID.

Examples:
* `add score -a P01 -n John Doe -s 12`
* `add score -a P02 -i E0123456 -s 12.5`

### Searching for students: `search`

Finds students who match the input keywords.

Format: `search (-n <student_name> | -i <student_id> | -g <group_name> | -t <tag>)`

* Search for students by their name, NUSNET ID, tag, or the group they belong in.
Only one type of tag should be used for each search.
* To search with multiple keywords, separate keywords with spaces. e.g. `search -g T02A R03C`
* The search is case-insensitive. e.g. `hans` will match `Hans`.
* The order of the keywords does not matter. e.g. `Hans Bo` will match `Bo Hans`.
* Persons matching at least one keyword will be returned (i.e. `OR` search).
e.g. `Hans Bo` will return `Hans Gruber`, `Bo Yang`.
* For search with name, only full words will be matched. e.g. `Han` will not match `Hans`.
* For search with NUSNET ID, group name or tag, partial search is supported. e.g. `T02` will match `T02A` and `T02B`.
`friend` will match `friends`.

Examples:
* `search -n John Doe`
* `search -i E0123456`
* `search -g T02B R03C`
* `search -t friends colleagues`

### Editing a student: `edit`

Edits the information of an existing student.

Format: `edit <index> [-n <student_name>] [-i <student_id>] [-g <group_name>]... [-t <tag>]...`

* Edits the person at the specified index. The index refers to the index number shown in the displayed person list. The index must be a positive integer 1, 2, 3, …​
* At least one of the optional fields must be provided.
* Existing values will be updated to the input values.
* When editing tags and groups, the existing tags and groups of the person will be removed i.e adding of tags and groups are not cumulative.
* You can remove all the person’s tags or groups by typing -t or -g without specifying any values after it.

Examples:
* `edit 1 -n John Doe`
* `edit 1 -n John Doe -i E1234567 -g T01 -g R01`  
* `edit 2 -t`

### Deleting a person : `delete`

Deletes the specified person from the address book.

Format: `delete <index>`

* Deletes the person at the specified `<index>`.
* The index refers to the index number shown in the displayed person list.
* The index **must be a positive integer** 1, 2, 3, …

Examples:
* `list` followed by `delete 2` deletes the 2nd person in the address book.
* `search -n Alex` followed by `delete 1` deletes the 1st person in the results of the `search` command.


### Importing data: `import`

Loads data as specified in the provided CSV file.

Format: `import -f <file_path> [-g <number_of_group_columns>] [-a <number_of_assessment_columns>] [-t <number_of_tag_columns>]`

* The file path can be either the absolute path or the relative path.
* The first row of the CSV file needs to be headers for the respective columns.
* The header for the assessment columns should the name of the assessment. For example, `Rune Trials`.
* The header for every other column does not matter.
* Every row apart from the first represents a student.
* The first two columns refer to the student’s name and NUSNET ID.
* The next i columns, where i is the specified number of group columns, refer to the student’s groups.
* The next j columns, where j is the specified number of assessment columns, refer to the student’s grade in the respective assessments.
* The next k columns, where k is the specified number of tag columns, refer to the student's tags.
* The number of group columns, assessment columns, and tag columns are assumed to be 0 if they are not specified.

<div markdown="block" class="alert alert-primary">

:bulb: **Tips:**<br>

* If the student does not have as many groups as the number of group columns, you should leave several group columns blank.

* If the student does not have a grade for some assessment, you should leave the corresponding assessment column blank.

</div>

Examples:
* `import -f /home/prof/CS1101S/student_data.csv -g 2 -a 10 -t 1`
* `import -f student_data.csv -g 3 -a 30`


### Resetting all data: `clear`

Clears all existing data.

Format: `clear`

### Closing the app: `exit`

Exits the application.

Format: `exit`

### Saving the data

SourceControl data are saved in the hard disk automatically after any command that changes the data. There is no need to save manually.

### Editing the data file

SourceControl data are saved as a JSON file `[JAR file location]/data/sourcecontrol.json`. Advanced users are welcome to update data directly by editing that data file.

<div markdown="span" class="alert alert-warning">:exclamation: **Caution:**
If your changes to the data file makes its format invalid, SourceControl will discard all data and start with an empty data file at the next run.
</div>

### Archiving data files `[coming soon]`

_Details coming soon ..._

--------------------------------------------------------------------------------------------------------------------

## FAQ `[coming soon]`

--------------------------------------------------------------------------------------------------------------------

## Command summary

Action | Format | Examples
--------|--------|----------
**Help** | `help` |
**List** | `list` |
**Add Student** | `add student -n <student_name> -i <student_id>  [-g <group_name>]...` | e.g. `add student -n Jane Doe -i E0123456 -g T01A -g R01A`
**Add Group** | `add group -g <group_name> [(-n <student_name> | -i <student_id>)]...` | e.g. `add group -g FG1`, `add group -g FG1 -n John Doe -i E0123456`
**Add Allocation** | `add alloc -g <group_name> (-n <student_name> | -i <student_id>)` | e.g. `add alloc -g T01A -n John Doe`, `add alloc -g T02A -i E0123456`
**Add Assessment** | `add assessment -a <assessment_name>` | e.g. `add assessment -a P01`
**Add Score** | `add score -a <assessment_name> (-n <student_name> | -i <student_id>) -s <score>` | e.g. `add score -a P01 -n John Doe -s 12`, `add score -a P02 -i E0123456 -s 12.5`
**Search** | `search (-n <student_name> | -i <student_id> | -g <group_name> | -t <tag>)` | e.g. `search -n John Doe` , `search -g T02B R04D`
**Edit Student** | `edit <index> [-n <student_name>] [-i <student_id>] [-g <group_name>]... [-t <tag>]...` | e.g.`edit 1 -n John Doe -i E1234567 -g T01 -g R01`
**Delete Student** | `delete <index>` | e.g. `delete 2`
**Import Data** | `import -f <file_path> [-g <number_of_group_columns>] [-a <number_of_assessment_columns>] [-t <number_of_tag_columns>]` | e.g. `import -f student_data.csv -g 2 -a 10 -t 1`
**Clear Data** | `clear` |
**Exit App** | `exit` |
