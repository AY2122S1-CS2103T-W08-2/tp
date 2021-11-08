---
layout: page
title: Nguyen Ba Van Nhi's Project Portfolio Page
---

### Project: Source Control

Source Control is a desktop application for CS1101S professors to manage the performance of their students. Users interact with it using a CLI, and it has a GUI created with JavaFX. It is written in Java, and has about 10 kLoC.

Given below are my contributions to the project.

* **New Feature** `addalloc`: Added the ability to add allocation
    * PR [\#117](https://git.io/JPAgf)
    * This feature is necessary for the target user to know which groups a student belongs to. It also serves as a follow-up feature of the add group feature.
    * This enhancement is the sole entrypoint for individual allocation of students to existing groups. The implementation was not trivial as a sub-class needed to be created to pass over the allocation information.

* **New Feature** `addscore`: Added the ability to add assessment score
    * PR [\#118](https://git.io/JPAw5), [\#243](https://git.io/JPAKh)
    * This feature is necessary for the target user to add and modify scores apart from bulk import.
    * This enhancement is the first feature that dealt with the modification of assessment data. The implementation was challenging due to the two-referral between assessments and students. Although the feature is quite similar to `addalloc` feature, a considerable amount of time was required for writing the code and tests.

* **New Feature** `show`: Added the ability to show summary information
    * PR [\#168](https://git.io/JPA6Y), [\#176](https://git.io/JPA6c), [\#177](https://git.io/JPA6l), [\#267](https://git.io/JPA6B)
    * This feature is necessary for the target user to view the summary information together with the distribution graph. 
    * This enhancement is the only feature that was implemented jointly by three different members. The implementation required considerable efforts to understand the graph implementation built by others in order to integrate supplementary parts into the complete feature.

* **Code contributed**: [RepoSense link](https://git.io/JPAzY)

<div style="page-break-after: always;"></div>

* **Project management**:
    * Released [`v1.3.demo` on GitHub](https://git.io/Ji2HC)

* **Enhancements to existing features**:
    * Adapted code from AB3 to suit the context of Source Control (PR [\#77](https://git.io/JPAa8), [\#78](https://git.io/JPAau), [\#80](https://git.io/JPAa2), [\#81](https://git.io/JPAaM), [\#85](https://git.io/JPAaH), [\#86](https://git.io/JPAad), [\#87](https://git.io/JPAaF), [\#89](https://git.io/JPAaA))
    * Maintained consistency of error messages (PR [\#244](https://git.io/JPAzp))

* **Documentation**:
    * User Guide:
        * Adapted content from AB3 to suit the context of Source Control (PR [\#56](https://git.io/JPAgt), [\#57](https://git.io/JPAgm))
        * Added documentation for the features `addalloc` and `addscore` (PR [\#58](https://git.io/JPAgY))
        * Made cosmetic enhancements jointly via PR reviews (PR [\#203](https://git.io/JPAgO))
    * Developer Guide:
        * Added implementation details of the `addalloc`, `addscore`, `show` features (PR [\#278](https://git.io/JXRf6))
        * Maintain the consistency between activity diagrams used in the guide (PR [\#283](https://git.io/JXRfk))

* **Community**:
    * PRs reviewed (with non-trivial review comments) (PR [\#203](https://git.io/JPAgO), [\#174](https://git.io/JPAg2))
    * Resolved merge conflicts by choice (PR [\#183](https://git.io/JPAgE))
    * Added stub classes and typical data used commonly as test utilities (PR [\#117](https://git.io/JPAgf), [\#130](https://git.io/JPAgI))
    * Reported bugs and suggestions for other teams in the class (see [\#325](https://git.io/JPAgC), [\#327](https://git.io/JPAgn), [\#307](https://git.io/JPAgs))
