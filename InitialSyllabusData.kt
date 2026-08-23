package com.example.data

import com.example.model.ChapterEntity
import com.example.model.ErrorNoteEntity
import com.example.model.HabitEntity
import com.example.model.LectureEntity
import com.example.model.MockTestEntity
import com.example.model.StudySessionEntity
import com.example.model.SubjectType
import com.example.model.TaskEntity
import com.example.model.UserProfileEntity

object InitialSyllabusData {

    fun getDefaultChapters(): List<ChapterEntity> {
        val list = mutableListOf<ChapterEntity>()

        // 1. PHYSICS (28 Chapters)
        val physicsList = listOf(
            Triple("Physical World", "Class 11", 2),
            Triple("Units & Measurements", "Class 11", 5),
            Triple("Motion in One Dimension", "Class 11", 6),
            Triple("Motion in Two Dimensions", "Class 11", 7),
            Triple("Laws of Motion", "Class 11", 9),
            Triple("Work Energy Power", "Class 11", 8),
            Triple("Circular Motion", "Class 11", 5),
            Triple("Gravitation", "Class 11", 6),
            Triple("Mechanical Properties of Solids", "Class 11", 5),
            Triple("Mechanical Properties of Fluids", "Class 11", 8),
            Triple("Thermal Properties of Matter", "Class 11", 6),
            Triple("Thermodynamics (Physics)", "Class 11", 7),
            Triple("Kinetic Theory", "Class 11", 4),
            Triple("Oscillations (SHM)", "Class 11", 7),
            Triple("Waves", "Class 11", 8),
            Triple("Electrostatics", "Class 12", 10),
            Triple("Current Electricity", "Class 12", 9),
            Triple("Moving Charges and Magnetism", "Class 12", 8),
            Triple("Magnetism and Matter", "Class 12", 4),
            Triple("Electromagnetic Induction", "Class 12", 6),
            Triple("Alternating Current", "Class 12", 6),
            Triple("Electromagnetic Waves", "Class 12", 3),
            Triple("Ray Optics", "Class 12", 11),
            Triple("Wave Optics", "Class 12", 7),
            Triple("Dual Nature of Radiation", "Class 12", 5),
            Triple("Atoms", "Class 12", 4),
            Triple("Nuclei", "Class 12", 4),
            Triple("Semiconductors", "Class 12", 7)
        )

        physicsList.forEachIndexed { index, (name, cls, lecs) ->
            val completed = index < 8
            val inProg = index in 8..11
            val questions = if (completed) 160 else if (inProg) 85 else 0
            list.add(
                ChapterEntity(
                    name = name,
                    subject = SubjectType.PHYSICS.name,
                    classLevel = cls,
                    totalLectures = lecs,
                    completedLectures = if (completed) lecs else if (inProg) lecs / 2 else 0,
                    questionsSolved = questions,
                    targetQuestions = 150,
                    revisionCount = if (completed) 2 else if (inProg) 1 else 0,
                    confidence = if (completed) 4 else if (inProg) 3 else 2,
                    isNcertRead = completed,
                    isPyqSolved = completed,
                    isFormulaNotesReady = completed || inProg,
                    isCompleted = completed
                )
            )
        }

        // 2. CHEMISTRY - PHYSICAL (7 Chapters)
        val chemPhysical = listOf(
            Triple("Mole Concept", "Class 11", 6),
            Triple("Atomic Structure", "Class 11", 7),
            Triple("Chemical Thermodynamics", "Class 11", 8),
            Triple("Equilibrium", "Class 11", 9),
            Triple("Electrochemistry", "Class 12", 8),
            Triple("Solutions", "Class 12", 6),
            Triple("Chemical Kinetics", "Class 12", 7)
        )
        chemPhysical.forEachIndexed { index, (name, cls, lecs) ->
            val completed = index < 4
            val inProg = index == 4
            list.add(
                ChapterEntity(
                    name = name,
                    subject = SubjectType.CHEMISTRY_PHYSICAL.name,
                    classLevel = cls,
                    totalLectures = lecs,
                    completedLectures = if (completed) lecs else if (inProg) 5 else 0,
                    questionsSolved = if (completed) 180 else if (inProg) 90 else 0,
                    targetQuestions = 160,
                    revisionCount = if (completed) 2 else 0,
                    confidence = if (completed) 5 else 3,
                    isNcertRead = completed,
                    isPyqSolved = completed,
                    isFormulaNotesReady = completed,
                    isCompleted = completed
                )
            )
        }

        // 3. CHEMISTRY - ORGANIC (8 Chapters)
        val chemOrganic = listOf(
            Triple("General Organic Chemistry (GOC)", "Class 11", 10),
            Triple("Hydrocarbons", "Class 11", 8),
            Triple("Haloalkanes & Haloarenes", "Class 12", 7),
            Triple("Alcohols, Phenols & Ethers", "Class 12", 8),
            Triple("Aldehydes, Ketones & Carboxylic Acids", "Class 12", 9),
            Triple("Amines", "Class 12", 6),
            Triple("Biomolecules", "Class 12", 5),
            Triple("Polymers & Practical Organic", "Class 12", 4)
        )
        chemOrganic.forEachIndexed { index, (name, cls, lecs) ->
            val completed = index < 3
            val inProg = index == 3
            list.add(
                ChapterEntity(
                    name = name,
                    subject = SubjectType.CHEMISTRY_ORGANIC.name,
                    classLevel = cls,
                    totalLectures = lecs,
                    completedLectures = if (completed) lecs else if (inProg) 4 else 0,
                    questionsSolved = if (completed) 175 else if (inProg) 70 else 0,
                    targetQuestions = 150,
                    revisionCount = if (completed) 2 else 0,
                    confidence = if (completed) 4 else 3,
                    isNcertRead = completed,
                    isPyqSolved = completed,
                    isFormulaNotesReady = completed,
                    isCompleted = completed
                )
            )
        }

        // 4. CHEMISTRY - INORGANIC (6 Chapters)
        val chemInorganic = listOf(
            Triple("Periodic Table & Periodicity", "Class 11", 6),
            Triple("Chemical Bonding", "Class 11", 9),
            Triple("Coordination Compounds", "Class 12", 8),
            Triple("d and f Block Elements", "Class 12", 6),
            Triple("p Block Elements", "Class 12", 7),
            Triple("s Block Elements", "Class 11", 4)
        )
        chemInorganic.forEachIndexed { index, (name, cls, lecs) ->
            val completed = index < 3
            list.add(
                ChapterEntity(
                    name = name,
                    subject = SubjectType.CHEMISTRY_INORGANIC.name,
                    classLevel = cls,
                    totalLectures = lecs,
                    completedLectures = if (completed) lecs else 0,
                    questionsSolved = if (completed) 140 else 0,
                    targetQuestions = 140,
                    revisionCount = if (completed) 2 else 0,
                    confidence = if (completed) 4 else 2,
                    isNcertRead = completed,
                    isPyqSolved = completed,
                    isFormulaNotesReady = completed,
                    isCompleted = completed
                )
            )
        }

        // 5. BIOLOGY - BOTANY (10 Chapters)
        val bioBotany = listOf(
            Triple("Cell: The Unit of Life", "Class 11", 7),
            Triple("Cell Cycle and Cell Division", "Class 11", 5),
            Triple("Plant Kingdom", "Class 11", 7),
            Triple("Morphology of Flowering Plants", "Class 11", 8),
            Triple("Anatomy of Flowering Plants", "Class 11", 6),
            Triple("Photosynthesis in Higher Plants", "Class 11", 7),
            Triple("Respiration in Plants", "Class 11", 6),
            Triple("Plant Growth & Development", "Class 11", 5),
            Triple("Principles of Inheritance (Genetics I)", "Class 12", 9),
            Triple("Molecular Basis of Inheritance (Genetics II)", "Class 12", 10),
            Triple("Biotechnology: Principles & Processes", "Class 12", 6),
            Triple("Organisms and Populations", "Class 12", 5),
            Triple("Ecosystem", "Class 12", 5),
            Triple("Biodiversity and Conservation", "Class 12", 4)
        )
        bioBotany.forEachIndexed { index, (name, cls, lecs) ->
            val completed = index < 9
            val inProg = index == 9
            list.add(
                ChapterEntity(
                    name = name,
                    subject = SubjectType.BIOLOGY_BOTANY.name,
                    classLevel = cls,
                    totalLectures = lecs,
                    completedLectures = if (completed) lecs else if (inProg) 6 else 0,
                    questionsSolved = if (completed) 220 else if (inProg) 110 else 0,
                    targetQuestions = 200,
                    revisionCount = if (completed) 3 else 1,
                    confidence = if (completed) 5 else 4,
                    isNcertRead = completed || inProg,
                    isPyqSolved = completed,
                    isFormulaNotesReady = completed,
                    isCompleted = completed
                )
            )
        }

        // 6. BIOLOGY - ZOOLOGY (11 Chapters)
        val bioZoology = listOf(
            Triple("Animal Kingdom", "Class 11", 8),
            Triple("Structural Organisation in Animals", "Class 11", 5),
            Triple("Digestion and Absorption", "Class 11", 5),
            Triple("Breathing and Exchange of Gases", "Class 11", 5),
            Triple("Body Fluids and Circulation", "Class 11", 6),
            Triple("Excretory Products and Elimination", "Class 11", 5),
            Triple("Locomotion and Movement", "Class 11", 5),
            Triple("Neural Control and Coordination", "Class 11", 7),
            Triple("Chemical Coordination & Integration", "Class 11", 6),
            Triple("Human Reproduction", "Class 12", 8),
            Triple("Reproductive Health", "Class 12", 4),
            Triple("Evolution", "Class 12", 7),
            Triple("Human Health and Disease", "Class 12", 7),
            Triple("Biotechnology and its Applications", "Class 12", 5)
        )
        bioZoology.forEachIndexed { index, (name, cls, lecs) ->
            val completed = index < 8
            val inProg = index == 8
            list.add(
                ChapterEntity(
                    name = name,
                    subject = SubjectType.BIOLOGY_ZOOLOGY.name,
                    classLevel = cls,
                    totalLectures = lecs,
                    completedLectures = if (completed) lecs else if (inProg) 3 else 0,
                    questionsSolved = if (completed) 240 else if (inProg) 100 else 0,
                    targetQuestions = 200,
                    revisionCount = if (completed) 3 else 1,
                    confidence = if (completed) 5 else 4,
                    isNcertRead = completed,
                    isPyqSolved = completed,
                    isFormulaNotesReady = completed,
                    isCompleted = completed
                )
            )
        }

        return list
    }

    fun getDefaultLectures(): List<LectureEntity> {
        return listOf(
            LectureEntity(
                chapterName = "Ray Optics",
                subject = SubjectType.PHYSICS.name,
                facultyName = "MR Sir",
                topic = "Lec 07: Lens Maker's Formula & Combinations",
                durationMinutes = 75,
                watchedPercentage = 80,
                isCompleted = false,
                dateString = "Today"
            ),
            LectureEntity(
                chapterName = "Molecular Basis of Inheritance",
                subject = SubjectType.BIOLOGY_BOTANY.name,
                facultyName = "Tarun Sir",
                topic = "Lec 05: Transcription & Post-transcriptional Modifications",
                durationMinutes = 90,
                watchedPercentage = 100,
                isCompleted = true,
                dateString = "Today"
            ),
            LectureEntity(
                chapterName = "Electrochemistry",
                subject = SubjectType.CHEMISTRY_PHYSICAL.name,
                facultyName = "Pankaj Sir",
                topic = "Lec 04: Nernst Equation & Concentration Cells",
                durationMinutes = 65,
                watchedPercentage = 60,
                isCompleted = false,
                dateString = "Tomorrow"
            ),
            LectureEntity(
                chapterName = "Chemical Coordination",
                subject = SubjectType.BIOLOGY_ZOOLOGY.name,
                facultyName = "Samapti Mam",
                topic = "Lec 03: Adrenal Gland & Pancreas Hormones",
                durationMinutes = 70,
                watchedPercentage = 100,
                isCompleted = true,
                dateString = "Yesterday"
            ),
            LectureEntity(
                chapterName = "Alcohols, Phenols & Ethers",
                subject = SubjectType.CHEMISTRY_ORGANIC.name,
                facultyName = "Amit Sir",
                topic = "Lec 02: Hydroboration Oxidation & Reimer Tiemann",
                durationMinutes = 80,
                watchedPercentage = 100,
                isCompleted = true,
                dateString = "Yesterday"
            )
        )
    }

    fun getDefaultTasks(): List<TaskEntity> {
        return listOf(
            TaskEntity(
                title = "Solve 45 Physics PYQs on Ray Optics (2018-2024)",
                subject = "Physics",
                priority = "HIGH",
                isCompleted = false,
                dueDate = "Today"
            ),
            TaskEntity(
                title = "Complete NCERT Line-by-Line Reading: Molecular Basis of Inheritance",
                subject = "Botany",
                priority = "HIGH",
                isCompleted = true,
                dueDate = "Today"
            ),
            TaskEntity(
                title = "Review Error Notebook: Electrochemistry standard reduction potential traps",
                subject = "Chemistry",
                priority = "MEDIUM",
                isCompleted = false,
                dueDate = "Today"
            ),
            TaskEntity(
                title = "Revise Animal Kingdom Charts & Examples tables 3 times",
                subject = "Zoology",
                priority = "MEDIUM",
                isCompleted = true,
                dueDate = "Today"
            ),
            TaskEntity(
                title = "Give Part Syllabus Mock Test 08 (Physics Mechanics + Botany Cell)",
                subject = "Mock Test",
                priority = "HIGH",
                isCompleted = false,
                dueDate = "Tomorrow"
            )
        )
    }

    fun getDefaultHabits(): List<HabitEntity> {
        return listOf(
            HabitEntity(
                name = "Wake up by 6:00 AM",
                iconKey = "sun",
                targetDaysPerWeek = 7,
                currentStreak = 14,
                historyMask = 0b1111111,
                isTodayCompleted = true
            ),
            HabitEntity(
                name = "8+ Hours Daily Study",
                iconKey = "clock",
                targetDaysPerWeek = 7,
                currentStreak = 21,
                historyMask = 0b1111111,
                isTodayCompleted = true
            ),
            HabitEntity(
                name = "NCERT Line by Line Reading",
                iconKey = "book",
                targetDaysPerWeek = 7,
                currentStreak = 18,
                historyMask = 0b0111111,
                isTodayCompleted = false
            ),
            HabitEntity(
                name = "Solve 100+ MCQs / Day",
                iconKey = "pencil",
                targetDaysPerWeek = 6,
                currentStreak = 9,
                historyMask = 0b1111101,
                isTodayCompleted = true
            ),
            HabitEntity(
                name = "Error Notebook Entry",
                iconKey = "fire",
                targetDaysPerWeek = 7,
                currentStreak = 12,
                historyMask = 0b0111111,
                isTodayCompleted = false
            ),
            HabitEntity(
                name = "Hydration & Health (3L Water)",
                iconKey = "water",
                targetDaysPerWeek = 7,
                currentStreak = 30,
                historyMask = 0b1111111,
                isTodayCompleted = true
            ),
            HabitEntity(
                name = "Sleep before 11:30 PM",
                iconKey = "moon",
                targetDaysPerWeek = 7,
                currentStreak = 8,
                historyMask = 0b1110111,
                isTodayCompleted = true
            )
        )
    }

    fun getDefaultMockTests(): List<MockTestEntity> {
        return listOf(
            MockTestEntity(
                testName = "All India Major Test 04 (Full Syllabus)",
                dateString = "18 Aug 2026",
                physicsScore = 158,
                chemistryScore = 165,
                biologyScore = 345,
                totalScore = 668,
                accuracyPercent = 91.5f,
                percentile = 99.1f,
                notes = "Great improvement in Botany! Need to speed up Optics calculations in Physics."
            ),
            MockTestEntity(
                testName = "Major Test 03 (Class 12th Full)",
                dateString = "10 Aug 2026",
                physicsScore = 145,
                chemistryScore = 160,
                biologyScore = 340,
                totalScore = 645,
                accuracyPercent = 88.2f,
                percentile = 98.4f,
                notes = "Solid organic chemistry performance. Lost 10 marks in Physics rotational motion."
            ),
            MockTestEntity(
                testName = "Part Test 02 (11th Mechanics & Physiology)",
                dateString = "02 Aug 2026",
                physicsScore = 140,
                chemistryScore = 155,
                biologyScore = 330,
                totalScore = 625,
                accuracyPercent = 86.0f,
                percentile = 97.2f,
                notes = "Good foundation, revision required for Human Physiology enzyme mechanisms."
            ),
            MockTestEntity(
                testName = "Diagnostic Starter Test 01",
                dateString = "20 Jul 2026",
                physicsScore = 130,
                chemistryScore = 145,
                biologyScore = 320,
                totalScore = 595,
                accuracyPercent = 81.4f,
                percentile = 94.8f,
                notes = "Baseline test score. Goal is 680+ for AIIMS Delhi."
            )
        )
    }

    fun getDefaultErrorNotes(): List<ErrorNoteEntity> {
        return listOf(
            ErrorNoteEntity(
                subject = "Physics",
                chapterName = "Ray Optics",
                questionSummary = "Focal length of lens dipped in liquid of higher refractive index",
                mistakeDescription = "Assumed focal length increases without sign change.",
                correctConcept = "When refractive index of medium > lens material, nature of lens reverses (converging becomes diverging) and focal length becomes negative.",
                status = "REVIEWING",
                tag = "Concept Trap"
            ),
            ErrorNoteEntity(
                subject = "Botany",
                chapterName = "Molecular Basis of Inheritance",
                questionSummary = "Difference between Leading and Lagging strand primers during replication",
                mistakeDescription = "Selected that DNA Polymerase synthesizes in 3'->5' direction on lagging strand.",
                correctConcept = "DNA Polymerase ALWAYS polymerizes in 5'->3' direction. Lagging strand synthesis is discontinuous creating Okazaki fragments, each needing an RNA primer.",
                status = "MASTERED",
                tag = "NCERT Line"
            ),
            ErrorNoteEntity(
                subject = "Physical Chemistry",
                chapterName = "Electrochemistry",
                questionSummary = "Calculation of standard EMF with multiplied stoichiometric coefficients",
                mistakeDescription = "Multiplied standard reduction potential E° by 2 when doubling half-cell reaction equation.",
                correctConcept = "E° is an INTENSIVE property! It does not depend on stoichiometric coefficients (unlike Delta G° which is extensive).",
                status = "UNSOLVED",
                tag = "Formula Slip"
            ),
            ErrorNoteEntity(
                subject = "Zoology",
                chapterName = "Neural Control",
                questionSummary = "Restoring Resting Potential after Action Potential",
                mistakeDescription = "Thought Na+ channels opening restores the resting membrane potential.",
                correctConcept = "Repolarization is caused by K+ efflux (K+ channels open, Na+ close). Na+/K+ ATPase active pump restores ionic concentration gradients.",
                status = "MASTERED",
                tag = "NCERT Line"
            )
        )
    }

    fun getDefaultSessions(): List<StudySessionEntity> {
        return listOf(
            StudySessionEntity(subject = "Physics", chapterName = "Ray Optics", durationMinutes = 50, mode = "50/10"),
            StudySessionEntity(subject = "Botany", chapterName = "Genetics II", durationMinutes = 75, mode = "Custom"),
            StudySessionEntity(subject = "Chemistry", chapterName = "Electrochemistry", durationMinutes = 50, mode = "50/10"),
            StudySessionEntity(subject = "Zoology", chapterName = "Physiology", durationMinutes = 25, mode = "25/5")
        )
    }

    fun getDefaultProfile(): UserProfileEntity {
        return UserProfileEntity(
            id = 1,
            name = "Aryan Sharma",
            targetScore = 690,
            targetCollege = "AIIMS New Delhi",
            currentStreak = 185,
            totalHoursStudied = 184.5f,
            totalXp = 5420,
            level = 15,
            dailyGoalHours = 8.0f,
            unlockedBadges = "STREAK_100,BIO_CHAMP,NIGHT_OWL,QUESTION_CRUSHER,MOCK_WARRIOR"
        )
    }
}
