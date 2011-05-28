<?xml version="1.0" encoding="iso-8859-2"?>
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
  <fo:layout-master-set>
    <fo:simple-page-master master-name="page"
			   page-width="297mm"
			   page-height="210mm"
			   margin-top="20mm"
			   margin-bottom="10mm"
			   margin-left="25mm"
			   margin-right="25mm">
      <fo:region-body margin-top="0mm" margin-bottom="0mm"
		      margin-left="24mm" margin-right="24mm"/>
      <fo:region-before extent="30mm"/>
      <fo:region-after extent="10mm"/>

    </fo:simple-page-master>
  </fo:layout-master-set>


  <fo:page-sequence master-reference="page">

    <!--HEADER-->
    <fo:static-content flow-name="xsl-region-before">
      <fo:block border-before-style="solid"
		border-after-style="solid">
	<fo:table border-collapse="collapse" table-layout="fixed" width="100%">
	  <fo:table-column column-number="1" column-width="50%"/>
	  <fo:table-column column-number="2" column-width="50%"/>

	  <fo:table-body>
	    <fo:table-row>
	      <fo:table-cell>
		<fo:block>
		  <fo:external-graphic src="images/sigla.png" content-height="20mm"/>
		</fo:block>
	      </fo:table-cell>

	      <fo:table-cell font-size="8pt" display-align="center">
		<fo:block text-align="center">BUCURESTI B-dul PRECIZIEI nr. 32, Sector 6</fo:block>
		<fo:block text-align="center">Tel.:  021.316.39.57, 021.316.05.78, 021.316.18.56 Tel/Fax: 021.316.05.90</fo:block>
		<fo:block text-align="center">e-mail: conducere@setumsa.ro; desfacere@setumsa.ro; contractari@setumsa.ro</fo:block>
                <fo:block text-align="center">www.setumsa.ro</fo:block>
	      </fo:table-cell>
	    </fo:table-row>
	  </fo:table-body>
	</fo:table>
      </fo:block>



    </fo:static-content>
    <!--/HEADER-->


    <!--FOOTER-->
    <fo:static-content flow-name="xsl-region-after">
      <fo:block text-align="end" font-size="8pt"
		border-after-style="solid">
	Pagina <fo:page-number/>
	din <fo:page-number-citation ref-id="last-page-block"/>
      </fo:block>
      <fo:block text-align="center" font-size="8pt">Nr. Reg.Com. J40/314/1991   CONT: RO94RNCB0077008452610001 B.C.R. Suc. Sector 6 
         C.F.:RO 458556; Capital Social: 1.415.115 LEI</fo:block>
    </fo:static-content>
    <!--/FOOTER-->





    <!-- BODY -->
    <fo:flow flow-name="xsl-region-body">

      <fo:block font-size="10pt" font-family="Times">


	<!-- date generale despre oferta (numar, data, etc.) -->
	<fo:block font-size="10pt" font-family="Times">
	  <fo:table width="16.1cm" border-collapse="collapse">
	    <fo:table-column column-number="1" column-width="25%"/>
	    <fo:table-column column-number="2" column-width="25%"/>


	    <fo:table-body>
	      <fo:table-row>
		<fo:table-cell column-number="1" border-style="solid">
		  <fo:block>Nr oferta: ${doc["response/record/field[@name='offerNo']"]}</fo:block>
		</fo:table-cell>
		<fo:table-cell column-number="2" border-style="solid">
		  <fo:block>Data: ${doc["response/record/field[@name='offerDateFrom']"]}</fo:block>
		</fo:table-cell>
	      </fo:table-row>
	      <fo:table-row>
		<fo:table-cell column-number="1" border-style="solid">
		  <fo:block></fo:block>
		</fo:table-cell>
		<fo:table-cell column-number="2" border-style="solid">
		  <fo:block>Nr. pagini: <fo:page-number-citation ref-id="last-page-block"/></fo:block>
		</fo:table-cell>
	      </fo:table-row>
	    </fo:table-body>
	  </fo:table>
	</fo:block>
	<!-- end date generale oferta -->


	<fo:block font-size="12pt" font-weight="bold">
	  ${doc["child::response/child::record/child::field[@name='offerName']"]}
	</fo:block>

	<fo:block page-break-after="always">
	<fo:table border-collapse="collapse">
	  <fo:table-column column-number="1" column-width="0.544cm"/>
	  <fo:table-column column-number="2" column-width="44.80cm"/>
	  <fo:table-column column-number="3" column-width="44.80cm"/>
	  <fo:table-column column-number="4" column-width="54.40cm"/>
	  <fo:table-column column-number="5" column-width="54.40cm"/>
	  <fo:table-column column-number="6" column-width="21.45cm"/>
	  <fo:table-column column-number="7" column-width="22.11cm"/>

	<!--
	    header    ... height 27
	    Nr. crt.           ...  w 17
	    Brosca             ...  w 140
	    Cilindru           ...  w 140
	    Sild               ...  w 170
	    Yalla              ...  w 170
	    Vizor              ...  w 65
	    Pret cu TVA (RON)  ...  w 67
	    

	    Inca o chestie: height 19
	    1. width 780
	    usa_code + " - " usa_name + " - " + usa_description 
	    
	-->
	<fo:table-body>
	  <fo:table-row>
	    <fo:table-cell column-number="1" border-style="solid"><fo:block>Nr. crt.</fo:block></fo:table-cell>
	    <fo:table-cell column-number="2" border-style="solid"><fo:block>Broasca</fo:block></fo:table-cell>
	    <fo:table-cell column-number="3" border-style="solid"><fo:block>Cilindru</fo:block></fo:table-cell>
	    <fo:table-cell column-number="4" border-style="solid"><fo:block>Sild</fo:block></fo:table-cell>
	    <fo:table-cell column-number="5" border-style="solid"><fo:block>Yalla</fo:block></fo:table-cell>
	    <fo:table-cell column-number="6" border-style="solid"><fo:block>Vizor</fo:block></fo:table-cell>
	    <fo:table-cell column-number="7" border-style="solid"><fo:block>Pret cu TVA (RON)</fo:block></fo:table-cell>
	  </fo:table-row>
	</fo:table-body>
	<!--
	    footer:
	    width 780
	    height 16
	    
	    expression: offerComment
	-->
	</fo:table>
      </fo:block> <!-- blocul pentru un tip de usi -->
      </fo:block> <!-- inchiderea blocului deschis la inceputul flow-lui -->


      <!-- Empty block, for page number citation -->
      <fo:block id="last-page-block"></fo:block>

    </fo:flow>
    <!-- /BODY -->
  </fo:page-sequence>
</fo:root>


<#-- search a value-list for a given value-id -->
<#function ssearch node vl_name>
 <#return search(node, vl_name, node.record["field[attribute::name='${vl_name}']"]) >
</#function>


<#-- search a value-list for a value coresponding to a value-id -->
<#function search node vl_name vl_key>


<#-- 
Searches the node for a child value-list with the given name, and in the
value list searches a child with the give key (value) and returns the associated label
-->


<#list node["value-list"] as vl>
	<#if vl["@name"] = vl_name>
		<#list vl["vl-item"] as vl_item>
			<#if vl_item.value = vl_key>
				<#return vl_item.label>
			</#if>
		</#list>
	</#if>
</#list>

<#return "N/A">

</#function>



<#function cvnumber text_number>
<#-- make the conversion to number -->
     <#return text_number?replace(",", ".")?number />
</#function> 

<#-- 2 macro-uri cu care pui virgula in fata unui element dintr-o enumerare
     doar daca nu este primul element din acea enumerare
-->
<#macro "enum_init" sep=",">
  <#assign first_elem = true>
  <#assign enum_separator = sep>
</#macro>

<#macro "enum_next">
  <#if first_elem == true><#assign first_elem = false><#else>${enum_separator}</#if>
</#macro>
