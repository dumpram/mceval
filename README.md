## Evaluation of Adaptive Mixed-Criticality Schedulability Tests

This repository contains evaluation of Adaptive Mixed-Criticaliy Schedulability algorithms. Implementations are Java based. Schedulability results
can be seen in results directory. We implemented following schedulability tests:

**AMC-rtb**, **AMC-max**, **SMC** and **SMC-no** implementations are based on: 

*S. K. Baruah, A. Burns and R. I. Davis, "Response-Time Analysis for Mixed Criticality Systems," 2011 IEEE 32nd Real-Time Systems Symposium, Vienna, 2011, pp. 34-43.*

**EDF-VD** implementation is based on:

*Sanjoy K. Baruah, Vincenzo Bonifaci, Gianlorenzo D'Angelo, Alberto Marchetti-Spaccamela, Suzanne Van Der Ster, and Leen Stougie. 2011. Mixed-criticality scheduling of sporadic task systems. In Proceedings of the 19th European conference on Algorithms (ESA'11), Camil Demetrescu and Magnús M. Halldórsson (Eds.). Springer-Verlag, Berlin, Heidelberg, 555-566.*

**EkbergGreedy** implementation is based on:

*P. Ekberg and W. Yi, "Outstanding Paper Award: Bounding and Shaping the Demand of Mixed-Criticality Sporadic Tasks," 2012 24th Euromicro Conference on Real-Time Systems, Pisa, 2012, pp. 135-144.*

**Exact** test is based on:

*Asyaban, S. & Kargahi, An exact schedulability test for fixed-priority preemptive mixed-criticality real-time systems, M. Real-Time Syst (2018) 54: 32. https://doi.org/10.1007/s11241-017-9287-2*

## Regenerate results

Run run.sh script. It starts SchedulabiltiyTest class. Generates 100 task set with 6 for each utilization with. Increasing
number of tasks increases execution time of exact exponentially. Therefore, evaluation can take some time with larger task sets.
