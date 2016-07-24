package hotareadetector.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import hotareadetector.data.CommitDataExtended;
import hotareadetector.data.VersionControlHistoryMetrics;

public class VersionControlHistoryMetricsCalculator {

	public Map<String, VersionControlHistoryMetrics> calculateVersionControlHistoryMetrics(Map<String, List<CommitDataExtended>> fileCommitMap, boolean isFocusNeeded) {
		Map<String, VersionControlHistoryMetrics> result = new HashMap<String, VersionControlHistoryMetrics>();
		Map<String, Double> focusWeightedContributions = null;
		if (isFocusNeeded) {
			focusWeightedContributions = (new ContributorFocusUtil()).calculateFocusWeightedOwnership(fileCommitMap);
		}
		for (Entry<String, List<CommitDataExtended>> fileCommitEntry : fileCommitMap.entrySet()) {
			VersionControlHistoryMetrics versionControlHistoryMetrics = new VersionControlHistoryMetrics();
			List<CommitDataExtended> commitDataExtendedList = fileCommitEntry.getValue();
			
			setModificationIntensityLikeMetrics(versionControlHistoryMetrics, commitDataExtendedList);
			setContributorLikeMetrics(versionControlHistoryMetrics, commitDataExtendedList);
			setDateLikeMetrics(versionControlHistoryMetrics, commitDataExtendedList);
			if (isFocusNeeded) {
				versionControlHistoryMetrics.setFocusWeightedContributors(focusWeightedContributions.get(fileCommitEntry.getKey()));
			}
			
			result.put(fileCommitEntry.getKey(), versionControlHistoryMetrics);
		}
		return result;
	}
	
	protected void setModificationIntensityLikeMetrics(VersionControlHistoryMetrics versionControlHistoryMetrics, List<CommitDataExtended> commitDataExtendedList) {
		versionControlHistoryMetrics.setNumberOfModifications(commitDataExtendedList.size());
		
		Integer churnSum = 0;
		for (CommitDataExtended commitDataExtended : commitDataExtendedList) {
			Integer actualChurnValue = commitDataExtended.getChurn();
			if (actualChurnValue != null) {
				churnSum += actualChurnValue;
			} else {
				churnSum = null;
				break;
			}
		}
		versionControlHistoryMetrics.setChurnValue(churnSum);
	}

	protected void setContributorLikeMetrics(
			VersionControlHistoryMetrics versionControlHistoryMetrics,
			List<CommitDataExtended> commitDataExtendedList) {
		List<String> contributorsList = retrieveContributors(commitDataExtendedList);
		Set<String> contributorsSet = new HashSet<String>();
		contributorsSet.addAll(contributorsList);
		
		versionControlHistoryMetrics.setNumberOfContributors(contributorsSet.size());
		
		List<String> contributorsToleranceOneList = new ArrayList<String>();
		contributorsToleranceOneList.addAll(contributorsList);		
		for (String contributor : contributorsSet) {
			contributorsToleranceOneList.remove(contributor);
		}
		Set<String> contributorsToleranceOneSet = new HashSet<String>();
		contributorsToleranceOneSet.addAll(contributorsToleranceOneList);
		versionControlHistoryMetrics.setNumberOfContributorsToleranceOne(contributorsToleranceOneSet.size());
		
		List<String> contributorsToleranceTwoList = new ArrayList<String>();
		contributorsToleranceTwoList.addAll(contributorsToleranceOneList);
		for (String contributor : contributorsToleranceOneSet) {
			contributorsToleranceTwoList.remove(contributor);
		}
		Set<String> contributorsToleranceTwoSet = new HashSet<String>();
		contributorsToleranceTwoSet.addAll(contributorsToleranceTwoList);
		versionControlHistoryMetrics.setNumberOfContributorsToleranceTwo(contributorsToleranceTwoSet.size());
	}

	protected List<String> retrieveContributors(List<CommitDataExtended> commitDataExtendedList) {
		List<String> result = new ArrayList<String>();
		for (CommitDataExtended commitDataExtended : commitDataExtendedList) {
			result.add(commitDataExtended.getCommitData().getContributor());
		}
		return result;
	}
	
	protected void setDateLikeMetrics(
			VersionControlHistoryMetrics versionControlHistoryMetrics,
			List<CommitDataExtended> commitDataExtendedList) {
		List<Date> modificationDates = retrieveDates(commitDataExtendedList);
		versionControlHistoryMetrics.setCreatingDate(modificationDates.get(0));
		// Using last but first, because the last modification is typically a mass rename, therefore having the same date for all files.
		versionControlHistoryMetrics.setLastModificationDate(modificationDates.size() > 2 ? modificationDates.get(modificationDates.size() - 2) : modificationDates.get(0));
		versionControlHistoryMetrics.setModificationDatesAverage(Calculator.calculateAverage(modificationDates));
	}

	protected List<Date> retrieveDates(List<CommitDataExtended> commitDataExtendedList) {
		List<Date> result = new ArrayList<Date>();
		for (CommitDataExtended commitDataExtended : commitDataExtendedList) {
			result.add(commitDataExtended.getCommitData().getDate());
		}
		return result;
	}
	
}
