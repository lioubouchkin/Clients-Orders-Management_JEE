
<fieldset>
	<legend>Informations commande</legend>

	<label for="dateCommande">Date <span class="requis">*</span></label>
	<input type="text" id="dateCommande" name="dateCommande"
		value='<c:out value="${ commande.date }"/>' size="20" maxlength="20"
		disabled /> <br />
		
	<label for="montantCommande">Montant <span
		class="requis">*</span></label>
	<input type="text" id="montantCommande" name="montantCommande"
		value='<c:out value="${ commande.montant }"/>' size="20" maxlength="20" />
	<span class="erreur"> <c:out value="${commandeForm.errors['montantCommande']}"/></span> <br />
		
	<label for="modePaiementCommande">Mode de paiement <span
		class="requis">*</span></label>
	<input type="text" id="modePaiementCommande" name="modePaiementCommande"
		value='<c:out value="${ commande.modePayement }"/>' size="20" maxlength="20" />
	<span class="erreur"> <c:out value="${commandeForm.errors['modePaiementCommande']}"/></span> <br />

	<label for="statutPaiementCommande">Statut du paiement</label>
	<input type="text" id="statutPaiementCommande" name="statutPaiementCommande"
		value='<c:out value="${ commande.statutPayement }"/>' size="20" maxlength="20" />
	<span class="erreur"> <c:out value="${commandeForm.errors['statutPaiementCommande']}"/></span> <br />
		
	<label for="modeLivraisonCommande">Mode de livraison <span
		class="requis">*</span></label>
	<input type="text" id="modeLivraisonCommande" name="modeLivraisonCommande"
		value='<c:out value="${ commande.modeLivraison }"/>' size="20" maxlength="20" />
	<span class="erreur"> <c:out value="${commandeForm.errors['modeLivraisonCommande']}"/></span> <br />

	<label for="statutLivraisonCommande">Statut de la livraison</label>
	<input type="text" id="statutLivraisonCommande" name="statutLivraisonCommande"
		value='<c:out value="${ commande.modeLivraison }"/>' size="20" maxlength="20" />
	<span class="erreur"> <c:out value="${commandeForm.errors['statutLivraisonCommande']}"/></span> <br />

	<p class="info"><c:out value="${ commandeForm.resultat }"/></p>
</fieldset>