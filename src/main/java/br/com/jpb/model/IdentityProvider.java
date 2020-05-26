package br.com.jpb.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum IdentityProvider {
	FACEBOOK(1) {
		@Override
		public String getProfilePicture(String userId) {
			return "https://graph.facebook.com/" + userId + "/picture";
		}
	},
	GOOGLE(2) {
		@Override
		public String getProfilePicture(String userId) {
			// TODO think about a way to get google picture
			return null;
		}
	};

	private final int order; // will choose the first when user has more than one providers

	public abstract String getProfilePicture(String userId);
}
