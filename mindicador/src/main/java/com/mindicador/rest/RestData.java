package com.mindicador.rest;

import java.util.Date;

import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.mindicador.model.ModelData;

@RestController
@RequestMapping(path = "rest/dataET")
public class RestData {

	@SuppressWarnings("deprecation")
	@GetMapping(path = "obtenerDatos", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ModelData getData(@RequestParam(name = "fecha") String fecha,
			@RequestParam(name = "iter") String iterador) {

		ModelData response = new ModelData();

		String result = "";
		JSONObject jsonObject = new JSONObject();
		JSONObject jsonObjectSeries = new JSONObject();

		final String uri = "https://mindicador.cl/api/" + iterador + "/" + fecha;
//		https://mindicador.cl/api/{tipo_indicador}/{dd-mm-yyyy}

		RestTemplate restTemplate = new RestTemplate();
		try {
			result = restTemplate.getForObject(uri, String.class);
		} catch (Exception e) {
			response.setStatusMessage("Parametros sin resultados");
			return response;
		}
		jsonObject = new JSONObject(result);
		jsonObjectSeries = new JSONObject(jsonObject.get("serie").toString().replace("[", "").replace("]", ""));

		String[] fechaArray = jsonObjectSeries.get("fecha").toString().split("T")[0].split("-");
		Date fechaOut = new Date(fechaArray[2] + "/" + fechaArray[1] + "/" + fechaArray[0]);
		response.setFecha(fechaOut);
		response.setIndicador(iterador);
		response.setValor(Double.parseDouble(jsonObjectSeries.get("valor").toString()));
		response.setStatusMessage("Respuesta OK");
		return response;
	}
}
