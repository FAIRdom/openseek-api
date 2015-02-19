package org.fairdom;

import ch.ethz.sis.openbis.generic.shared.api.v3.IApplicationServerApi;
import ch.ethz.sis.openbis.generic.shared.api.v3.dto.entity.dataset.DataSet;
import ch.ethz.sis.openbis.generic.shared.api.v3.dto.entity.experiment.Experiment;
import ch.ethz.sis.openbis.generic.shared.api.v3.dto.entity.sample.Sample;
import ch.ethz.sis.openbis.generic.shared.api.v3.dto.fetchoptions.dataset.DataSetFetchOptions;
import ch.ethz.sis.openbis.generic.shared.api.v3.dto.fetchoptions.experiment.ExperimentFetchOptions;
import ch.ethz.sis.openbis.generic.shared.api.v3.dto.fetchoptions.sample.SampleFetchOptions;
import ch.ethz.sis.openbis.generic.shared.api.v3.dto.search.DataSetSearchCriterion;
import ch.ethz.sis.openbis.generic.shared.api.v3.dto.search.ExperimentSearchCriterion;
import ch.ethz.sis.openbis.generic.shared.api.v3.dto.search.SampleSearchCriterion;
import ch.systemsx.cisd.openbis.generic.shared.api.v3.json.GenericObjectMapper;

import java.io.StringWriter;
import java.util.List;

/**
 * Created by quyennguyen on 13/02/15.
 */
public class OpenbisQuery {

    public static void main(String[] args) {
        OpenbisQuery query = new OpenbisQuery();
        String result = query.jsonResult(args[0], args[1]);
        try {
            System.out.println(result);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
            System.exit(-1);
        }
        System.exit(0);
    }

    public List <Experiment> experiments(String SeekID){
        ExperimentSearchCriterion criterion = new ExperimentSearchCriterion();
        criterion.withProperty("SEEK_STUDY_ID").thatEquals(SeekID);

        ExperimentFetchOptions options = new ExperimentFetchOptions();
        options.withProperties();
        options.withProject();

        Authentication au = new Authentication("https://openbis-testing.fair-dom.org/openbis", "api-user", "api-user");
        IApplicationServerApi api = au.api();
        String sessionToken = au.authentication();

        List <Experiment> experiments = api.searchExperiments(sessionToken, criterion, options);
        return experiments;
    }

    public String jsonResult(String type, String SeekID){
        GenericObjectMapper mapper = new GenericObjectMapper();
        StringWriter sw = new StringWriter();
        try {
            if (type == "Experiment"){
                mapper.writeValue(sw, experiments(SeekID));
            }else if (type == "Sample"){
                mapper.writeValue(sw, samples(SeekID));
            }else{
                mapper.writeValue(sw, dataSets(SeekID));
            }

        }catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        return sw.toString();
    }

    public List <Sample> samples(String SeekID){
        SampleSearchCriterion criterion = new SampleSearchCriterion();
        criterion.withExperiment().withProperty("SEEK_STUDY_ID").thatEquals(SeekID);

        SampleFetchOptions options = new SampleFetchOptions();
        options.withProperties();
        options.withExperiment().withProperties();

        Authentication au = new Authentication("https://openbis-testing.fair-dom.org/openbis", "api-user", "api-user");
        IApplicationServerApi api = au.api();
        String sessionToken = au.authentication();

        List <Sample> samples = api.searchSamples(sessionToken, criterion, options);
        return samples;
    }

    public List <DataSet> dataSets(String SeekID){
        DataSetSearchCriterion criterion = new DataSetSearchCriterion();
        criterion.withExperiment().withProperty("SEEK_STUDY_ID").thatEquals(SeekID);

        DataSetFetchOptions options = new DataSetFetchOptions();
        options.withProperties();
        options.withExperiment().withProperties();

        Authentication au = new Authentication("https://openbis-testing.fair-dom.org/openbis", "api-user", "api-user");
        IApplicationServerApi api = au.api();
        String sessionToken = au.authentication();

        List <DataSet> dataSets = api.searchDataSets(sessionToken, criterion, options);
        return dataSets;
    }

}
