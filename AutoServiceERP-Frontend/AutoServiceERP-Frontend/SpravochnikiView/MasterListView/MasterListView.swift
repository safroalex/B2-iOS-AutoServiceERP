//
//  MasterListView.swift
//  AutoServiceERP-Frontend
//
//  Created by Александр Сафронов on 26.11.2023.
//

import SwiftUI

struct MasterListView: View {
    @State private var masters: [Master] = []
    @State private var showingAddMasterView = false

    var body: some View {
        List {
            ForEach(masters, id: \.id) { master in
                Text(master.name)
            }
        }
        .navigationTitle("Мастера")
        .toolbar {
            ToolbarItemGroup(placement: .navigationBarTrailing) {
                Button(action: { fetchMasters() }) {
                    Text("Обновить")
                }
                Button(action: { showingAddMasterView = true }) {
                    Image(systemName: "plus")
                }
            }
        }
        .sheet(isPresented: $showingAddMasterView, onDismiss: fetchMasters) {
            AddMasterView(masters: $masters)
        }
    }

    private func fetchMasters() {
        NetworkManager.shared.fetchMasters { fetchedMasters in
            self.masters = fetchedMasters
        }
    }
}


