package org.fairdom.openseekapi.facility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONObject;

import ch.ethz.sis.openbis.generic.asapi.v3.dto.dataset.DataSet;
import ch.ethz.sis.openbis.generic.asapi.v3.dto.experiment.Experiment;
import ch.ethz.sis.openbis.generic.asapi.v3.dto.sample.Sample;
import ch.ethz.sis.openbis.generic.asapi.v3.dto.space.Space;
import ch.ethz.sis.openbis.generic.asapi.v3.dto.tag.Tag;
import ch.ethz.sis.openbis.generic.dssapi.v3.dto.datasetfile.DataSetFile;

/**
 * @author Stuart Owen
 */

public class JSONCreator {

	private String json;

	public JSONCreator(List<?> result) {
		Map<String, List<Map<String, Object>>> map = new HashMap<String, List<Map<String, Object>>>();
		for (Object item : result) {
			if (item instanceof Experiment) {
				if (!map.containsKey("experiments")) {
					map.put("experiments", new ArrayList<Map<String, Object>>());
				}
				map.get("experiments").add(jsonMap((Experiment) item));
			}
			if (item instanceof DataSet) {
				if (!map.containsKey("datasets")) {
					map.put("datasets", new ArrayList<Map<String, Object>>());
				}
				map.get("datasets").add(jsonMap((DataSet) item));
			}
			if (item instanceof Sample) {
				if (!map.containsKey("samples")) {
					map.put("samples", new ArrayList<Map<String, Object>>());
				}
				map.get("samples").add(jsonMap((Sample) item));
			}
			if (item instanceof Space) {
				if (!map.containsKey("spaces")) {
					map.put("spaces", new ArrayList<Map<String, Object>>());
				}
				map.get("spaces").add(jsonMap((Space) item));
			}
			if (item instanceof DataSetFile) {
				if (!map.containsKey("datasetfiles")) {
					map.put("datasetfiles", new ArrayList<Map<String, Object>>());
				}
				map.get("datasetfiles").add(jsonMap((DataSetFile) item));
			}
		}

		json = JSONObject.toJSONString(map);
	}

	public String getJSON() {
		return json;
	}

	private Map<String, Object> jsonMap(DataSet dataset) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("permId", dataset.getPermId().getPermId());
		map.put("code", dataset.getCode());
		map.put("properties", dataset.getProperties());
		map.put("modificationDate", dataset.getModificationDate().toString());
		map.put("registrationDate", dataset.getRegistrationDate().toString());
		if (dataset.getModifier() != null)
			map.put("modifier", dataset.getModifier().getUserId());
		else
			map.put("modifier", null);

		if (dataset.getRegistrator() != null) {
			map.put("registerator", dataset.getRegistrator().getUserId());
		} else {
			map.put("registerator", null);
		}

                if (dataset.getExperiment() != null) {
                    map.put("experiment", dataset.getExperiment().getPermId().getPermId());
                };
                if (dataset.getSample() != null) {
                    map.put("sample",dataset.getSample().getPermId().getPermId());
                }
                
		map.put("tags", tagList(dataset.getTags()));

		Map<String, String> dsType = new HashMap<String, String>();
		if (dataset.getType() != null) {
			if (dataset.getType().getDescription() != null) {
				dsType.put("description", dataset.getType().getDescription());
			} else {
				dsType.put("description", "");
			}
			if (dataset.getType().getCode() != null) {
				dsType.put("code", dataset.getType().getCode());
			} else {
				dsType.put("code", "");
			}
		}
		map.put("dataset_type", dsType);

		List<String> sampleIds = new ArrayList<String>();
		if (dataset.getSample() != null) {
			sampleIds.add(dataset.getSample().getPermId().getPermId());
		}
		map.put("samples", sampleIds);
		return map;
	}

	private Map<String, Object> jsonMap(DataSetFile datasetFile) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("dataset", datasetFile.getDataSetPermId().getPermId());
		map.put("filePermId", datasetFile.getPermId().toString());
		map.put("path", datasetFile.getPath());
		map.put("isDirectory", datasetFile.isDirectory());
		map.put("fileLength", datasetFile.getFileLength());
		return map;
	}

	private Map<String, Object> jsonMap(Experiment experiment) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("permId", experiment.getPermId().getPermId());
		map.put("code", experiment.getCode());
		map.put("project", experiment.getProject().getPermId().getPermId());
		map.put("properties", experiment.getProperties());
		map.put("modificationDate", experiment.getModificationDate().toString());
		map.put("registrationDate", experiment.getRegistrationDate().toString());
		map.put("identifier", experiment.getIdentifier().getIdentifier()); // E.g.
																			// /API-SPACE/API-PROJECT/E2
		if (experiment.getModifier() != null)
			map.put("modifier", experiment.getModifier().getUserId());
		else
			map.put("modifier", null);

		if (experiment.getRegistrator() != null) {
			map.put("registerator", experiment.getRegistrator().getUserId());
		} else {
			map.put("registerator", null);
		}

		map.put("tags", tagList(experiment.getTags()));

		Map<String, String> expType = new HashMap<String, String>();
		if (experiment.getType() != null) {
			if (experiment.getType().getDescription() != null) {
				expType.put("description", experiment.getType().getDescription());
			} else {
				expType.put("description", "");
			}
			if (experiment.getType().getCode() != null) {
				expType.put("code", experiment.getType().getCode());
			} else {
				expType.put("code", "");
			}
		}
		map.put("experiment_type", expType);
		List<String> sampleIds = new ArrayList<String>();
		for (Sample sample : experiment.getSamples()) {
			sampleIds.add(sample.getPermId().getPermId());
		}
		map.put("samples", sampleIds);
		List<String> datasetIds = new ArrayList<String>();
		for (DataSet dataset : experiment.getDataSets()) {
			datasetIds.add(dataset.getPermId().getPermId());
		}
		map.put("datasets", datasetIds);
		return map;
	}

	private Map<String, Object> jsonMap(Sample sample) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("permId", sample.getPermId().getPermId());
		map.put("code", sample.getCode());
		map.put("properties", sample.getProperties());
		map.put("identifier", sample.getIdentifier().getIdentifier());
		map.put("modificationDate", sample.getModificationDate().toString());
		map.put("registrationDate", sample.getRegistrationDate().toString());
		if (sample.getModifier() != null)
			map.put("modifier", sample.getModifier().getUserId());
		else
			map.put("modifier", null);

		if (sample.getRegistrator() != null) {
			map.put("registerator", sample.getRegistrator().getUserId());
		} else {
			map.put("registerator", null);
		}

		Map<String, String> sampleType = new HashMap<String, String>();
		if (sample.getType() != null) {
			if (sample.getType().getDescription() != null) {
				sampleType.put("description", sample.getType().getDescription());
			} else {
				sampleType.put("description", "");
			}
			if (sample.getType().getCode() != null) {
				sampleType.put("code", sample.getType().getCode());
			} else {
				sampleType.put("code", "");
			}
			sampleType.put("code", sample.getType().getCode());
		}
		map.put("sample_type", sampleType);

		if (sample.getExperiment() != null) {
			map.put("experiment", sample.getExperiment().getPermId().getPermId());
		}
		map.put("tags", tagList(sample.getTags()));
		List<String> datasetIds = new ArrayList<String>();
		for (DataSet dataset : sample.getDataSets()) {
			datasetIds.add(dataset.getPermId().getPermId());
		}
		map.put("datasets", datasetIds);
		return map;
	}

	private Map<String, Object> jsonMap(Space space) {
		Map<String, Object> map = new HashMap<>();
		map.put("permId", space.getPermId().getPermId());
		map.put("code", space.getCode());
		map.put("description", space.getDescription());
		List<String> projectIds = new ArrayList<>();
		List<String> experimentIds = new ArrayList<>();
		List<String> datasetIds = new ArrayList<>();
                List<String> sampleIds = new ArrayList<>();
                
                // removed by TZ there is no longer need for fetching space objects with the space
                /*
		for (Project project : space.getProjects()) {
			projectIds.add(project.getPermId().getPermId());
			for (Experiment experiment : project.getExperiments()) {
				String id = experiment.getPermId().getPermId();
				if (!experimentIds.contains(id)) {
					experimentIds.add(id);
				}
				for (DataSet dataset : experiment.getDataSets()) {
					String dataId = dataset.getPermId().getPermId();
					if (!datasetIds.contains(dataId)) {
						datasetIds.add(dataId);
					}
				}
			}
		}
                for (Sample sample : space.getSamples()) {
                    if (!sampleIds.contains(sample.getPermId().getPermId())) {
                        sampleIds.add(sample.getPermId().getPermId());
                    }
                    for (DataSet dataset: sample.getDataSets()) {
                        String dataId = dataset.getPermId().getPermId();
                        if (!datasetIds.contains(dataId)) {
                                datasetIds.add(dataId);
                        }                        
                    }
                }*/
		map.put("projects", projectIds);
		map.put("experiments", experimentIds);
		map.put("datasets", datasetIds);
                map.put("samples", sampleIds);

		map.put("modificationDate", space.getModificationDate().toString());
		map.put("registrationDate", space.getRegistrationDate().toString());
		return map;
	}

	private List<String> tagList(Set<Tag> tags) {
		List<String> tagStr = new ArrayList<String>();
		for (Tag tag : tags) {
			tagStr.add(tag.getCode());
		}
		return tagStr;
	}

}
