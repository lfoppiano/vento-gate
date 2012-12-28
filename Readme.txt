Readme on the GATE library

The library contains already, in the test resources, two directory for the configuration:
 - gate-project-classification
 - gate-project-training

and two models:
 - vento-senti-brain
 - vento-senti-brain.training

They have to be copied all the the same directory as they have crossed references each other:

 - gate-project-classification contains
    1) batch-learning.classification.configuration.xml referring to vento-senti-brain (classification)
    2) batch-learning.evaluation.configuration.xml referring to vento-senti-brain.training (evaluation/testing)
 - gate-project-training contains
    1) batch-learning.training.configuration.xml referring to vento-senti-brain.training (training)


