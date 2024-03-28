package org.openapitools.codegen.languages;

import org.openapitools.codegen.*;
import org.openapitools.codegen.config.GlobalSettings;
import org.openapitools.codegen.model.ModelMap;
import org.openapitools.codegen.model.ModelsMap;
import org.openapitools.codegen.model.OperationMap;
import org.openapitools.codegen.model.OperationsMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class GoAutumrestClientCodegen extends GoClientCodegen implements CodegenConfig {
    private final Logger LOGGER = LoggerFactory.getLogger(GoAutumrestClientCodegen.class);

    private static final String GENERATED_FILENAME_PREFIX = "generated_";

    private final Set<String> allReferencedModels = new HashSet<>();
    private List<ModelMap> allModels = new ArrayList<>();

    public String getName() {
        return "go-autumrest";
    }

    public String getHelp() {
        return "Generates a go-autumrest client.";
    }

    public GoAutumrestClientCodegen() {
        super();

        embeddedTemplateDir = templateDir = "go-autumrest";

        apiTestTemplateFiles.clear();
        modelDocTemplateFiles.clear();
        apiDocTemplateFiles.clear();

        modelFileFolder = "";
    }

    @Override
    public String toApiFilename(String name) {
        return GENERATED_FILENAME_PREFIX + super.toApiFilename(name);
    }

    @Override
    public String toModelFilename(String name) {
        return GENERATED_FILENAME_PREFIX + super.toModelFilename(name);
    }

    @Override
    public void processOpts() {
        super.processOpts();
        supportingFiles.clear();

        supportingFiles.add(new SupportingFile("base_client.mustache", "", GENERATED_FILENAME_PREFIX + "base_client.go"));
        supportingFiles.add(new SupportingFile("client_error.mustache", "", GENERATED_FILENAME_PREFIX + "client_error.go"));
    }

    @Override
    public ModelsMap postProcessModels(ModelsMap objs) {
        objs = super.postProcessModels(objs);

        List<Map<String, String>> imports = objs.getImports();

        for (ModelMap m : objs.getModels()) {
            CodegenModel model = m.getModel();
            if (model.isEnum) {
                continue;
            }

            // additionalProperties: true and parent
            if (model.isAdditionalPropertiesTrue && model.parent != null && Boolean.FALSE.equals(model.isMap)) {
                //remove this imports again
                imports.remove(createMapping("import", "reflect"));
            }

            //remove this imports again
            imports.remove(createMapping("import", "fmt"));
            if (model.hasRequired) {
                if (!model.isAdditionalPropertiesTrue &&
                        (model.oneOf == null || model.oneOf.isEmpty()) &&
                        (model.anyOf == null || model.anyOf.isEmpty())) {
                    //remove this imports again
                    imports.remove(createMapping("import", "bytes"));
                }
            }

            m.put("hasImports", !imports.isEmpty());
        }
        return objs;
    }

    @Override
    public OperationsMap postProcessOperationsWithModels(OperationsMap objs, List<ModelMap> allModels) {
        objs = super.postProcessOperationsWithModels(objs, allModels);

        // remove model imports to avoid error
        List<Map<String, String>> imports = objs.getImports();
        if (imports == null)
            return objs;

        OperationMap objectMap = objs.getOperations();
        List<CodegenOperation> operations = objectMap.getOperation();

        for (CodegenOperation operation : operations) {
            for (CodegenParameter param : operation.allParams) {
                // import "reflect" package if the parameter is collectionFormat=multi
                if (param.isCollectionFormatMulti) {
                    //TODO temporally remove this imports again
                    imports.remove(createMapping("import", "reflect"));
                    break;
                }
            }
        }

        String apiNames = GlobalSettings.getProperty("apis");
        if (apiNames != null && !apiNames.isEmpty()) {
            final Map<String, Collection<String>> model2Imports = new HashMap<>();
            for (ModelMap modelMap : allModels) {
                CodegenModel model = modelMap.getModel();
                model2Imports.put(model.getClassname(), model.getImports());
            }

            for (CodegenOperation operation : operations) {
                for (String modelImport : operation.imports) {
                    addModels(modelImport, model2Imports);
                }
            }
            this.allModels = new ArrayList<>(allModels);
        }
        return objs;
    }

    private void addModels(String model, Map<String, Collection<String>> model2Imports) {
        allReferencedModels.add(model);

        Collection<String> importedModels = model2Imports.get(model);
        if (importedModels != null) {
            for (String importedModel : importedModels) {
                if (!allReferencedModels.contains(importedModel)) {
                    addModels(importedModel, model2Imports);
                }
            }
        }
    }

    @Override
    public void postProcess() {
        super.postProcess();

        String apiNames = GlobalSettings.getProperty("apis");
        if (apiNames != null && !apiNames.isEmpty()) {
            final Set<String> nonReferencedModelFilenames = new HashSet<>();
            for (ModelMap modelMap : allModels) {
                String className = modelMap.getModel().getClassname();
                if (!allReferencedModels.contains(className)) {
                    for (String templateName : modelTemplateFiles().keySet()) {
                        String modelName = modelMap.getModel().getName();
                        String filename = modelFilename(templateName, modelName.replaceAll("_+$", ""));
                        nonReferencedModelFilenames.add(filename);
                    }
                }
            }

            for (String filename : nonReferencedModelFilenames) {
                try {
                    Path path = Path.of(filename);
                    if (Files.deleteIfExists(path)) {
                        LOGGER.info("drop unreferenced model-file {} again", path);
                    } else {
                        LOGGER.info("drop unreferenced model-file {} again, but it doesn't exists", path);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
